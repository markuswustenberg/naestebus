var app = {
    ROOT: location.protocol + '//' + location.hostname,
    MAP_CENTER: new google.maps.LatLng(56.172113, 10.188317),

    map: null,
    infoWindow: new google.maps.InfoWindow(),
    me: null,
    stops: {}
}

function Stop(data) {
    this.data = data;
    this.marker = new google.maps.Marker({
        position: new google.maps.LatLng(data.latitude / 1000000, data.longitude / 1000000),
        title: data.name,
        map: app.map,
        icon: app.ROOT + "/img/littlebus.png"
    });
    var self = this;
    google.maps.event.addListener(this.marker, 'click', function() {
        self.updateAndShowInfoWindow();
    });
}
Stop.prototype.updateAndShowInfoWindow = function() {
    var self = this;
    $.getJSON(app.ROOT + "/departures?stopId=" + this.data.id + '&max=5', function(departures) {
        var content = '';
        departures.forEach(function(departure) {
            content += '<p><strong>' + departure.name + '</strong> ' + departure.time;
            if (departure.hasDirection) {
                content += ' → ' + departure.direction;
            }
            content += '</p>';
        });
        updateInfoWindow(self.marker, content);
    });
}

function Me(position) {
    // Add marker
    this.marker = new google.maps.Marker({
        position: position,
        map: app.map,
        icon: app.ROOT + "/img/littleperson.png",
        draggable: true
    });

    // Add info window telling the user this marker can be moved
    updateInfoWindow(this.marker, 'You can move me!');

    // Add position listener
    var self = this;
    google.maps.event.addListener(this.marker, 'dragend', function() {
        self.onPositionUpdate();
    });

    this.onPositionUpdate();
}
Me.prototype.setPosition = function(position) {
    this.marker.position = position;
    this.onPositionUpdate();
}
Me.prototype.onPositionUpdate = function() {
    var position = this.marker.getPosition();
    app.map.panTo(position);
    fetchStops(position);
}

function initialize() {
    var mapOptions = {
        mapTypeControlOptions: {
            mapTypeIds: [google.maps.MapTypeId.ROADMAP, 'grayscale']
        },
        center: app.MAP_CENTER,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        zoom: 15
    };
    app.map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
    var styles = [
        {
            "stylers": [
                { "saturation": -100 }
            ]
        }
    ];
    var mapType = new google.maps.StyledMapType(styles, { name:"Gray" });
    app.map.mapTypes.set('grayscale', mapType);
    app.map.setMapTypeId('grayscale');

    // Close info window if clicked outside
    google.maps.event.addListener(app.map, "click", function(event) {
        app.infoWindow.close();
    });

    // Add me
    app.me = new Me(app.map.getCenter());

    // Find current location if possible and center
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
            app.me.setPosition(pos);
        });
    }
}

function updateInfoWindow(marker, content) {
    // Close the previous one, set content, and open again with new content
    app.infoWindow.close();
    app.infoWindow.setContent(content);
    app.infoWindow.open(app.map, marker);
}

function fetchStops(position) {
    // Fetch and add stop markers
    $.getJSON(app.ROOT + "/stops?latitude=" + ((position.lat() * 1000000) | 0) + "&longitude=" + ((position.lng() * 1000000) | 0) + "&radius=1000&max=5", function(data) {
        data.forEach(function(datum) {
            // Check if the marker is already present
            var key = datum.latitude.toString() + datum.longitude.toString();
            if (key in app.stops) {
                return;
            }

            app.stops[key] = new Stop(datum);
        });
    });
}

google.maps.event.addDomListener(window, 'load', initialize);

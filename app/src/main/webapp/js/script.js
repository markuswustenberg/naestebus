var app = {
    stops: {}
}

function Me(map) {
    // Add marker
    this.map = map;
    this.marker = new google.maps.Marker({
        position: map.getCenter(),
        map: map,
        icon: "/img/littleperson.png",
        draggable: true
    });

    // Add info window telling the user this marker can be moved
    var infoWindow = new google.maps.InfoWindow({content: 'You can move me!'});
    infoWindow.open(map, this.marker);
    setTimeout(function() {
        infoWindow.close();
    }, 3000);

    // Add position listener
    var self = this;
    google.maps.event.addListener(this.marker, 'dragend', function() {
        self.onPositionUpdate();
    });

    this.onPositionUpdate();
}
Me.prototype.onPositionUpdate = function() {
    this.map.panTo(this.marker.getPosition());
    this.findStops();
}
Me.prototype.findStops = function() {
    // Find and add stop markers
    var position = this.marker.getPosition();
    var map = this.map;
    $.getJSON("/stops?latitude=" + ((position.lat() * 1000000) | 0) + "&longitude=" + ((position.lng() * 1000000) | 0) + "&radius=1000&max=5", function(stops) {
        stops.forEach(function(stop) {
            // Check if the marker is already present
            var key = stop.latitude.toString() + stop.longitude.toString();
            if (key in app.stops) {
                return;
            }
            app.stops[key] = new Stop(map, stop);
        });
    });
}

function Stop(map, data) {
    this.map = map;
    this.data = data;
    this.marker = new google.maps.Marker({
        position: new google.maps.LatLng(data.latitude / 1000000, data.longitude / 1000000),
        title: data.name,
        map: map,
        icon: "/img/littlebus.png"
    });
    var self = this;
    google.maps.event.addListener(this.marker, 'click', function() {
        self.updateAndShowInfoWindow();
    });
}
Stop.infoWindow = new google.maps.InfoWindow();
Stop.prototype.updateAndShowInfoWindow = function() {
    var self = this;
    $.getJSON("/departures?stopId=" + this.data.id + '&max=5', function(departures) {
        var content = '';
        departures.forEach(function(departure) {
            content += '<p><strong>' + departure.name + '</strong> ' + departure.time;
            if (departure.hasDirection) {
                content += ' → ' + departure.direction;
            }
            content += '</p>';
        });

        // Close the previous one, set content, and open again with new content
        Stop.infoWindow.close();
        Stop.infoWindow.setContent(content);
        Stop.infoWindow.open(self.map, self.marker);
    });
}

function initialize() {
    var map = new google.maps.Map(document.getElementById("map-canvas"), {
        mapTypeControlOptions: {
            mapTypeIds: [google.maps.MapTypeId.ROADMAP, 'grayscale']
        },
        center: new google.maps.LatLng(56.172113, 10.188317),
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        zoom: 15
    });
    map.mapTypes.set('grayscale', new google.maps.StyledMapType([{stylers: [{saturation: -100}]}], {name: "Gray"}));
    map.setMapTypeId('grayscale');

    // Close stop info windows if clicked outside
    google.maps.event.addListener(map, "click", function(event) {
        Stop.infoWindow.close();
    });

    // Find current location if possible
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            map.setCenter(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));
            new Me(map);
        });
    } else {
        new Me(map);
    }
}

google.maps.event.addDomListener(window, 'load', initialize);

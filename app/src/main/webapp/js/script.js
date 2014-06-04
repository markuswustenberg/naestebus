var app = {
    SEARCH_RADIUS: 500,

    me: null,
    stops: {}
};

function Me(map) {
    // Add marker
    this.map = map;
    this.marker = new google.maps.Marker({
        position: map.getCenter(),
        map: map,
        icon: "/img/littleperson.png",
        draggable: true
    });

    var self = this;

    // Add info window telling the user this marker can be moved
    this.infoWindow = new google.maps.InfoWindow({content: 'You can move me!'});
    this.infoWindow.open(map, this.marker);
    this.timeout = setTimeout(function() {
        self.infoWindow.close();
    }, 3000);

    // Add circle around me showing the bus stop search radius
    this.circle = new google.maps.Circle({
        center: this.marker.getPosition(),
        clickable: false,
        fillColor: "#ffffff",
        fillOpacity: 0.3,
        map: map,
        radius: app.SEARCH_RADIUS,
        strokeColor: "#000000",
        strokeOpacity: 0,
        zIndex: -10
    });

    // Add position listener
    google.maps.event.addListener(this.marker, 'dragend', function() {
        self.onPositionUpdate();
    });

    this.onPositionUpdate();
}
Me.prototype.onPositionUpdate = function() {
    this.circle.setCenter(this.marker.getPosition());
    this.map.panTo(this.marker.getPosition());
    this.findStops();
};
Me.prototype.findStops = function() {
    // Find and add stop markers
    var position = this.marker.getPosition();
    var self = this;
    $.ajax("/stops?latitude=" + ((position.lat() * 1000000) | 0) + "&longitude=" + ((position.lng() * 1000000) | 0) + "&radius=" + app.SEARCH_RADIUS + "&max=50", {
        success: function(stops) {
            stops.forEach(function(stop) {
                // Check if the marker is already present
                var key = stop.coordinate.latitude.toString() + stop.coordinate.longitude.toString();
                if (key in app.stops) {
                    return;
                }
                app.stops[key] = new Stop(self.map, stop);
            });
        },
        error: function() {
            self.showErrorInfoWindow();
        }
    });
};
Me.prototype.showErrorInfoWindow = function() {
    clearTimeout(this.timeout);
    this.infoWindow.close();
    this.infoWindow.setContent("Sorry! There was an error finding buses. I'm officially lost.");
    this.infoWindow.open(this.map, this.marker);
};

function Stop(map, data) {
    this.map = map;
    this.data = data;
    this.marker = new google.maps.Marker({
        position: new google.maps.LatLng(data.coordinate.latitude / 1000000, data.coordinate.longitude / 1000000),
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
    $.ajax("/departures?stopId=" + this.data.id + '&max=5', {
        success: function(departures) {
            var content = '';
            departures.forEach(function(departure) {
                content += '<p><strong>' + departure.name + '</strong> ' + departure.time;
                if (departure.hasDirection) {
                    content += ' → ' + departure.direction;
                }
                content += '</p>';
            });

            // If content is empty, no departures are available
            if (!content) {
                content = '<p>No departures anytime soon.</p>';
            } else {
                // Otherwise, add data source disclaimer
                content += '<p>Data by <a href="http://www.rejseplanen.dk">Rejseplanen</a> under <a href="http://creativecommons.org/licenses/by-nd/3.0/">Creative Commons</a></p>';
            }

            // Close the previous one, set content, and open again with new content
            Stop.infoWindow.close();
            Stop.infoWindow.setContent(content);
            Stop.infoWindow.open(self.map, self.marker);
        },
        error: function() {
            app.me.showErrorInfoWindow();
        }
    });
};

function initialize() {
    var map = new google.maps.Map(document.getElementById("map-canvas"), {
        panControl: false,
        zoomControl: false,
        mapTypeControl: false,
        streetViewControl: false,
        scaleControl: false,
        overviewMapControl: false,
        center: new google.maps.LatLng(56.153014, 10.203261),
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        zoom: 15
    });
    // See http://gmaps-samples-v3.googlecode.com/svn/trunk/styledmaps/wizard/index.html for styles
    map.mapTypes.set('grayscale', new google.maps.StyledMapType([
        {
            "stylers": [
                { "saturation": -100 }
            ]
        },{
            "featureType": "transit.station.bus",
            "stylers": [
                { "visibility": "off" }
            ]
        }
    ], {name: "Gray"}));
    map.setMapTypeId('grayscale');

    // Close stop info windows if clicked outside
    google.maps.event.addListener(map, "click", function() {
        Stop.infoWindow.close();
    });

    // Find current location if possible
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            map.setCenter(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));
            app.me = new Me(map);
        });
    } else {
        app.me = new Me(map);
    }
}

google.maps.event.addDomListener(window, 'load', initialize);

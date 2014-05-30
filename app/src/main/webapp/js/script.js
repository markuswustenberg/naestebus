
var app = {
    ROOT: location.protocol + '//' + location.hostname,
    MAP_CENTER: new google.maps.LatLng(56.172113, 10.188317),

    ME_INFO_WINDOW_CONTENT: 'You can move me!',
    ME_ICON: "/img/littleperson.png",

    STOP_ICON: "/img/littlebus.png",

    map: null,
    infoWindow: new google.maps.InfoWindow(),
    me: {
        marker: null
    },
    stops: {}
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

    findLocationAndCenter();
}

function findLocationAndCenter() {
    // Find current location if possible
    if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
            app.map.setCenter(pos);
            addMeMarker(pos);
        });
    } else {
        // Set marker at current map center if no geolocation possible
        addMeMarker(app.map.getCenter());
    }
}

function addMeMarker(position) {
    // Add marker
    app.me.marker = new google.maps.Marker({
        position: position,
        map: app.map,
        icon: app.ROOT + app.ME_ICON,
        draggable: true
    });

    // Add info window telling the user this marker can be moved
    updateInfoWindow(app.me.marker, app.ME_INFO_WINDOW_CONTENT);

    // Fetch stops immediately
    fetchStops(position);

    // Add listener for when app.me.marker is moved
    addMyPositionChangedListener();
}

function addMyPositionChangedListener() {
    // Add position listener
    google.maps.event.addListener(app.me.marker, 'dragend', function() {
        var position = app.me.marker.getPosition();
        app.map.panTo(position);
        fetchStops(position);
    });
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

            // Add marker
            var stopPosition = new google.maps.LatLng(datum.latitude / 1000000, datum.longitude / 1000000);
            var marker = new google.maps.Marker({
                position: stopPosition,
                title: datum.name,
                map: app.map,
                icon: app.ROOT + app.STOP_ICON
            });

            app.stops[key] = marker;
            google.maps.event.addListener(marker, 'click', function() {
                showBusMarkerInfoWindow(datum, marker);
            });
        });
    });
}

function showBusMarkerInfoWindow(stop, marker) {
    $.getJSON(app.ROOT + "/departures?stopId=" + stop.id + '&max=5', function(departures) {
        var content = '';
        departures.forEach(function(departure) {
            content += '<p><strong>' + departure.name + '</strong> ' + departure.time;
            if (departure.hasDirection) {
                content += ' → ' + departure.direction;
            }
            content += '</p>';
        });
        updateInfoWindow(marker, content);
    });
}

google.maps.event.addDomListener(window, 'load', initialize);

var markers = [];
var meMarker = null;
var map;

function initialize() {
    var styles = [
        {
            "elementType": "labels",
            "stylers": [
                { "visibility": "off" }
            ]
        },{
            "stylers": [
                { "saturation": -100 }
            ]
        }
    ];
    var mapOptions = {
        mapTypeControlOptions: {
            mapTypeIds: [google.maps.MapTypeId.ROADMAP, 'grayscale']
        },
        center: new google.maps.LatLng(56.172113, 10.188317),
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        zoom: 15
    };
    map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);

    var mapType = new google.maps.StyledMapType(styles, { name:"Grayscale" });
    map.mapTypes.set('grayscale', mapType);
    map.setMapTypeId('grayscale');

    findLocationAndCenter();
}

function findLocationAndCenter() {
    // Find current location if possible
    if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
            map.setCenter(pos);
            addMeMarker(pos);
        });
    } else {
        // Set marker at current map center if no geolocation possible
        addMeMarker(map.getCenter());
    }
}

function addMeMarker(position) {
    // Add marker
    meMarker = new google.maps.Marker({
        position: position,
        map: map,
        icon: location.protocol + '//' + location.hostname + "/img/littleperson.png",
        draggable: true
    });

    // Add info window telling the user this marker can be moved
    new google.maps.InfoWindow({
        content: 'You can move me!'
    }).open(map, meMarker);

    // Fetch stops immediately
    fetchStops(position);

    // Add listener for when meMarker is moved
    addPositionChangedListener();
}

function addPositionChangedListener() {
    // Add position listener
    google.maps.event.addListener(meMarker, 'dragend', function() {
        var position = meMarker.getPosition();
        map.panTo(position);
        fetchStops(position);
    });
}

function fetchStops(position) {
    // Fetch and add stop markers
    $.getJSON(location.protocol + '//' + location.hostname + "/stops?latitude=" + ((position.lat() * 1000000) | 0) + "&longitude=" + ((position.lng() * 1000000) | 0) + "&radius=1000&max=20", function(stops) {
        // Clear previous stop markers
        markers.forEach(function(marker) {
            marker.setMap(null);
        });

        stops.forEach(function(stop) {
            var stopPosition = new google.maps.LatLng(stop.latitude / 1000000, stop.longitude / 1000000);
            markers.push(new google.maps.Marker({
                position: stopPosition,
                map: map,
                icon: location.protocol + '//' + location.hostname + "/img/littlebus.png"
            }));
        });
    });
}

google.maps.event.addDomListener(window, 'load', initialize);

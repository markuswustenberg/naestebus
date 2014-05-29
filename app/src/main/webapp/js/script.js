var markers = [];

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
    var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);

    var mapType = new google.maps.StyledMapType(styles, { name:"Grayscale" });
    map.mapTypes.set('grayscale', mapType);
    map.setMapTypeId('grayscale');

    findLocationAndCenter(map);

    addIdleEventListener(map);
}

function findLocationAndCenter(map) {
    // Find current location if possible
    if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
            map.setCenter(pos);
        });
    }
}

function addIdleEventListener(map) {
    // Add a listener to when map is idle after movement
    google.maps.event.addListener(map, 'idle', function() {
        // Clear previous stop markers
        markers.forEach(function(marker) {
            marker.setMap(null);
        });

        // Get new center
        var center = map.getCenter();

        // Add "me" marker
        // See https://developers.google.com/chart/image/docs/gallery/dynamic_icons#icon_list
        markers.push(new google.maps.Marker({position: center, map: map, icon: "https://chart.googleapis.com/chart?chst=d_map_pin_icon&chld=glyphish_walk%7CFF0000"}));

        console.log("test");

        // Fetch and add stop markers
        $.getJSON(location.protocol + '//' + location.hostname + "/stops?latitude=" + ((center.lat() * 1000000) | 0) + "&longitude=" + ((center.lng() * 1000000) | 0) + "&radius=1000&max=10", function(stops) {
            stops.forEach(function(stop) {
                var position = new google.maps.LatLng(stop.latitude / 1000000, stop.longitude / 1000000);
                markers.push(new google.maps.Marker({position: position, map: map, icon: "https://chart.googleapis.com/chart?chst=d_map_pin_icon&chld=star%7C00FF00"}));
            });
        });
    });
}

google.maps.event.addDomListener(window, 'load', initialize);
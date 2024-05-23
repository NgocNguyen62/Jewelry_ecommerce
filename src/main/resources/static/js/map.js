
const address = {
            name: "Vị trí của bạn",
            location: {
                lat: 20.962269,
                lng: 105.779687
            }
        };



var map;
var marker;
var marker2;
var line;
var redIcon = L.icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
    iconSize: [25, 41], // Kích thước của biểu tượng (icon)
    iconAnchor: [12, 41], // Vị trí gắn icon (tâm của biểu tượng)
    popupAnchor: [1, -34] // Vị trí hiển thị popup
});
function initMap() {
    map = L.map('map_div').setView([21.0168864, 105.7855574], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);

    marker = L.marker([21.0168864, 105.7855574], { icon: redIcon }).addTo(map);

    marker.bindPopup("<b>Jewelry shop</b>").openPopup();
    map.on('click', onMapClick);
    // addMarker(address.location.lat, address.location.lng, address.name);
}

function addMarker(lat, lng, popupContent) {
    var marker2 = L.marker([lat, lng]).addTo(map);

    marker2.bindPopup(popupContent).openPopup();
    return marker2;
}
function updateMarker(market, lat, lng, name){
    market.setLatLng([lat, lng]).update();
    market.bindPopup(name).openPopup();
}

document.addEventListener('DOMContentLoaded', function() {
    initMap();
    var receiverAddressInput = document.getElementById('address');

    receiverAddressInput.addEventListener('blur', function() {
        var addressValue = receiverAddressInput.value;
        if (addressValue.trim() !== '') {
            search(addressValue).then(function(result) {
                console.log(marker2._latlng);
                console.log(marker._latlng);

                direct(marker2._latlng, marker._latlng).then(function (r){
                    console.log("complete")
                })

            });
        }
    });
});



var popup = L.popup();

function onMapClick(e) {
    popup
        .setLatLng(e.latlng)
        .setContent("You clicked the map at " + e.latlng.toString())
        .openOn(map);
}

async function search (searchKey) {
    const settings = {
        async: true,
        crossDomain: true,
        url: `https://trueway-geocoding.p.rapidapi.com/Geocode?address=${encodeURIComponent(searchKey)}&language=en`,
        method: 'GET',
        headers: {
            'X-RapidAPI-Key': '9d27715100mshfc9dda491ef9b88p1352ffjsn819f8ba10464',
            'X-RapidAPI-Host': 'trueway-geocoding.p.rapidapi.com'
        }
    };
    var location;
    var name;
    await $.ajax(settings).done(function (response) {
        if (response.results && response.results.length > 0) {
            console.log(response.results[0].location);
            var location = response.results[0].location;
            var address = response.results[0].address;
            // addMarker(response.results[0].location.lat, response.results[0].location.lng, response.results[0].address)
            if (marker2) {
                updateMarker(marker2, location.lat, location.lng, address);
            } else {
                marker2 = L.marker([location.lat, location.lng]).addTo(map);
                marker2.bindPopup(address).openPopup();
            }
            location = [response.results[0].location.lat, response.results[0].location.lng];
            name = response.results[0].address;
            // return [response.results[0].location.lat, response.results[0].location.lng, response.results[0].address];
        } else {
            console.log("Không tìm thấy địa chỉ phù hợp.");
            var message = L.popup();
            message.setLatLng(marker.getLatLng());
            message.setContent("Không tìm thấy địa chỉ phù hợp");
            message.addTo(map);
            marker2 = null;
        }

    });
    // return [location, name];

}
async function direct(location1, location2) {
    console.log('------------------------------------------------------------');
    console.log(location1);
    console.log(location2);
    console.log('{\r\n    "coordinates": [\r\n        [\r\n            ' + location1.lat + ',\r\n            ' + location1.lng + '\r\n        ],\r\n        [\r\n            ' + location2.lat + ',\r\n            ' + location2.lng + '\r\n        ]\r\n    ],\r\n');
    console.log('{\r\n    "coordinates": [\r\n        [\r\n            8.673706054687502,\r\n            49.688954878870305\r\n        ],\r\n        [\r\n            8.68611663579941,\r\n            49.6814429662868\r\n        ]\r\n    ],\r\n')

    const settings = {
        async: true,
        crossDomain: true,
        url: 'https://maptoolkit.p.rapidapi.com/route?points=' + location1.lat + '%2C' + location1.lng + '%7C' + location2.lat + '%2C' + location2.lng + '&routeType=car',
        method: 'GET',
        headers: {
            'X-RapidAPI-Key': 'ff6aa73df3mshf047d38b40e4eacp1d29a2jsn8f76ce7599c0',
            'X-RapidAPI-Host': 'maptoolkit.p.rapidapi.com'
        }
    };


    await $.ajax(settings).done(function (response) {
        console.log(response);
        console.log('------------------------------------------------------------');
        console.log(location1);
        console.log(location2);
        // Lấy danh sách các điểm thuộc đoạn đường
        const coordinates = response.paths[0].instructions;
        console.log("????????????");
        console.log(coordinates);
        let latlngs = [];
        // latlngs.unshift([location1.lat, location1.lng]);

        // Hiển thị các điểm
        coordinates.forEach((point, index) => {
            // console.log(`Point ${index + 1}: Longitude: ${point.coordinate[0]}, Latitude: ${point.coordinate[0]}`);
            latlngs.push([point.coordinate[0], point.coordinate[1]]);

        });
        // latlngs.push([location2.lat, location2.lng]);
        console.log(latlngs);

        // Display the polyline on the map
        if (line) {
            line.setLatLngs(latlngs);
            map.fitBounds(line.getBounds());
            console.log(line);
        } else {
            line = L.polyline(latlngs, {color: 'red'}).addTo(map);
            map.fitBounds(line.getBounds());
            console.log(line);
        }
    });
}

// let start = [marker2._latlng.lat, marker2._latlng.lng],
//     end = [marker._latlng.lat, marker._latlng.lng];
// let url = new URL("https://maptoolkit.p.rapidapi.com/route");
// url.searchParams.append("point", `${start[1]},${start[0]}`);
// url.searchParams.append("point", `${end[1]},${end[0]}`);
// url.searchParams.append("routeType", "car");
// url.searchParams.append("rapidapi-key", "ff6aa73df3mshf047d38b40e4eacp1d29a2jsn8f76ce7599c0");
// fetch(url)
//     .then((r) => r.json())
//     .then((route) => {
//         let path = route.paths[0];
//
//         // Add route polyline to map
//         let coordinates = polyline.decode(path.points);
//         new L.Polyline(coordinates, {color: "#2a3561", weight: 5}).addTo(map);
//
//         // Add instruction markers with popup to map
//         path.instructions.forEach((instruction) => {
//             let marker = new L.Marker(instruction.coordinate, {
//                 icon: new L.Icon({
//                     iconUrl: "https://static.maptoolkit.net/sprites/maptoolkit/route-via.svg",
//                     iconSize: [12, 12],
//                     iconAnchor: [6, 6]
//                 })
//             }).addTo(map);
//             marker.bindPopup(L.popup().setContent(`<p>${instruction.text}</p>`));
//         });
//     })

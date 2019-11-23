<#include 'base.ftlh'>

<#macro page_head>
    <title>Team Nitrobrew</title>
</#macro>

<#macro page_body>
    <div id="app">
        <video id="selfview"></video>
        <video id="remoteview"></video>
    </div>
    <div id="cover">
        <p>Waiting on a tutor/tutee...</p>
    </div>
    <div id="rating-modal" class="modal">
        <form action="/rate" method="post">
            <div class="rate">
                <input type="radio" id="star5" name="rate" value="5"/>
                <label for="star5" title="text">5 stars</label>
                <input type="radio" id="star4" name="rate" value="4"/>
                <label for="star4" title="text">4 stars</label>
                <input type="radio" id="star3" name="rate" value="3"/>
                <label for="star3" title="text">3 stars</label>
                <input type="radio" id="star2" name="rate" value="2"/>
                <label for="star2" title="text">2 stars</label>
                <input type="radio" id="star1" name="rate" value="1"/>
                <label for="star1" title="text">1 star</label>
            </div>
            <input id="tutor-id" type="hidden" value="">
            <input type="submit" value="Submit rating">
        </form>
    </div>
</#macro>

<#macro content>
    <@page_body/>
</#macro>

<#macro scripts>
    <@common_scripts/>
    <script src="https://js.pusher.com/4.1/pusher.min.js"></script>
    <script>
        function go() {
            document.getElementById("cover").remove();
        }

        function tryPlay(e) {
            if (!e.playing) {
                e.play();
            }
            setTimeout(() => {
                tryPlay(e)
            }, 200)
        }

        function tryPlayAll() {
            tryPlay(document.getElementById("selfview"));
            tryPlay(document.getElementById("remoteview"));
        }

        tryPlayAll();

        function getUrlParameter(name) {
            name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
            var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
            var results = regex.exec(location.search);
            return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
        };

        var pusher = new Pusher("d964cc6cb9f216c957f8", {
            cluster: "us2",
            encrypted: true,
            authEndpoint: "/pusher/auth"
        });
        let role = getUrlParameter("role");
        let id, tutorId, sessionDesc, room, caller, localUserMedia;
        const channel = pusher.subscribe(getUrlParameter("room"));

        let remoteVideo = document.getElementById("remoteview");
        let selfVideo = document.getElementById("selfview");

        channel.bind('pusher:subscription_succeeded', (members) => {
            console.log("subsuc: " + members.count);
            id = channel.members.me.id;
            startWebRTC(members.count === 2);
        });

        channel.bind('pusher:member_added', (member) => {
            console.log("ADDED: " + member.id);
        });

        channel.bind('pusher:member_removed', (member) => {
            endCurrentCall();
        });

        function endCurrentCall() {
            console.log("ending call");
        }

        function getCam() {
            //Get local audio/video feed and show it in selfview video element
            return navigator.mediaDevices.getUserMedia({
                video: true,
                audio: true
            });
        }
        function GetRTCIceCandidate() {
            window.RTCIceCandidate = window.RTCIceCandidate || window.webkitRTCIceCandidate ||
                window.mozRTCIceCandidate || window.msRTCIceCandidate;
            return window.RTCIceCandidate;
        }
        function GetRTCPeerConnection() {
            window.RTCPeerConnection = window.RTCPeerConnection || window.webkitRTCPeerConnection ||
                window.mozRTCPeerConnection || window.msRTCPeerConnection;
            return window.RTCPeerConnection;
        }
        function GetRTCSessionDescription() {
            window.RTCSessionDescription = window.RTCSessionDescription || window.webkitRTCSessionDescription ||
                window.mozRTCSessionDescription || window.msRTCSessionDescription;
            return window.RTCSessionDescription;
        }


        let pc;

        function startWebRTC(isOfferer) {
            pc = new RTCPeerConnection();

            // 'onicecandidate' notifies us whenever an ICE agent needs to deliver a
            // message to the other peer through the signaling server
            pc.onicecandidate = event => {
                if (event.candidate) {
                    channel.trigger('client-onicecandidate', {'candidate': event.candidate});
                }
            };

            // If user is offerer let the 'negotiationneeded' event create the offer
            if (isOfferer) {
                pc.onnegotiationneeded = () => {
                    pc.createOffer().then(localDescCreated).catch(e => console.error(e));
                }
            }

            // When a remote stream arrives display it in the #remoteVideo element
            pc.ontrack = event => {
                const stream = event.streams[0];
                if (!remoteVideo.srcObject || remoteVideo.srcObject.id !== stream.id) {
                    remoteVideo.srcObject = stream;
                }
            };

            navigator.mediaDevices.getUserMedia({
                audio: true,
                video: true,
            }).then(stream => {
                // Display your local video in #localVideo element
                selfVideo.srcObject = stream;
                go();
                // Add your stream to be sent to the conneting peer
                stream.getTracks().forEach(track => pc.addTrack(track, stream));
            }, e => { console.error(e) });

            channel.bind('client-onicecandidate', (data) => {
                console.log("onice");
                pc.addIceCandidate(
                    new RTCIceCandidate(data.candidate)
                );
            });

            channel.bind('client-onsdp', (data) => {
                console.log("onsdp");
                pc.setRemoteDescription(new RTCSessionDescription(data.sdp)).then(() => {
                    // When receiving an offer lets answer it
                    if (pc.remoteDescription.type === 'offer') {
                        pc.createAnswer().then(localDescCreated).catch(e => console.error(e));
                    }
                });
            });

            function localDescCreated(desc) {
                pc.setLocalDescription(desc).then(() => channel.trigger('client-onsdp', {'sdp': pc.localDescription}))
                    .catch(e => console.error(e));
            }
        }
    </script>
</#macro>

<@display_page/>
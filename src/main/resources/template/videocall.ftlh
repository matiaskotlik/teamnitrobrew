<#include 'base.ftlh'>

<#macro page_head>
    <title>Team Nitrobrew</title>
    <style>
        body {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: space-evenly;
            height: 100%;
        }
        #app {
            display: flex;
            margin: 0;
            align-items: flex-start;
            justify-content: center;
            padding: 0 50px;
            font-family: -apple-system, BlinkMacSystemFont, sans-serif;
        }
        video {
            display: block;
            max-width: 500px;
            margin: 0;
            box-sizing: border-box;
            border-radius: 2px;
            padding: 0;
            box-shadow: rgba(156, 172, 172, 0.2) 0px 2px 2px, rgba(156, 172, 172, 0.2) 0px 4px 4px, rgba(156, 172, 172, 0.2) 0px 8px 8px, rgba(156, 172, 172, 0.2) 0px 16px 16px, rgba(156, 172, 172, 0.2) 0px 32px 32px, rgba(156, 172, 172, 0.2) 0px 64px 64px;
        }
        .bbpanel {
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
</#macro>

<#macro page_body>
    <div id="app">
        <video id="selfview"></video>
        <video id="remoteview"></video>
    </div>
    <div class="bbpanel">
        <button style="width: 100px" class="btn-sm button" onclick="endCurrentCall()">End Call</button>
    </div>
    <div id="cover">
        <!DOCTYPE html>
        <html>
        <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style>
        /* Center the loader */
        #loader {
          position: absolute;
          left: 50%;
          top: 50%;
          z-index: 1;
          width: 150px;
          height: 150px;
          margin: -75px 0 0 -75px;
          border: 16px solid #f3f3f3;
          border-radius: 50%;
          border-top: 16px solid #90AF77;
          width: 120px;
          height: 120px;
          -webkit-animation: spin 2s linear infinite;
          animation: spin 2s linear infinite;
        }

        @-webkit-keyframes spin {
          0% { -webkit-transform: rotate(0deg); }
          100% { -webkit-transform: rotate(360deg); }
        }

        @keyframes spin {
          0% { transform: rotate(0deg); }
          100% { transform: rotate(360deg); }
        }

        /* Add animation to "page content" */
        .animate-bottom {
          position: relative;
          -webkit-animation-name: animatebottom;
          -webkit-animation-duration: 5000s;
          animation-name: animatebottom;
          animation-duration: 5000s
        }

        @-webkit-keyframes animatebottom {
          from { bottom:-100px; opacity:0 }
          to { bottom:0px; opacity:1 }
        }

        @keyframes animatebottom {
          from{ bottom:-100px; opacity:0 }
          to{ bottom:0; opacity:1 }
        }

        #myDiv {
          display: none;
          text-align: center;
        }
        </style>
        </head>
        <body onload="myFunction()" style="margin:0;">

        <div id="loader"></div>

        <script>
        var myVar;

        function myFunction() {
          myVar = setTimeout(showPage, 3000);
        }

        function showPage() {
          document.getElementById("loader").style.display = "none";
          document.getElementById("myDiv").style.display = "block";
        }
        </script>

        </body>
        </html>

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
            // authEndpoint: "/pusher/auth"
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
            // startWebRTC(false);
        });

        channel.bind('pusher:member_removed', (member) => {
            endCurrentCall();
        });

        channel.bind('client-tid', (data) => {
            tutorId = data.id;
        });

        function endCurrentCall() {
            console.log("ending call");
            pc.close();
            if (role === "tutee") {
                window.location = "/rate?id=" + tutorId;
            } else {
                window.location = "/";
            }
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
                console.log('ontrak');
                const stream = event.streams[0];
                if (!remoteVideo.srcObject || remoteVideo.srcObject.id !== stream.id) {
                    remoteVideo.srcObject = stream;
                }

                if (role === "tutor") {
                    channel.trigger("client-tid", {id: getUrlParameter("id")});
                }
            };

            navigator.mediaDevices.getUserMedia({
                audio: true,
                video: true,
            }).then(stream => {
                // Display your local video in #localVideo element
                selfVideo.srcObject = stream;
                // Add your stream to be sent to the conneting peer
                let sender;
                stream.getTracks().forEach(track => sender = pc.addTrack(track, stream));
            }, e => { console.error(e) });

            channel.bind('client-onicecandidate', (data) => {
                console.log("onice");
                go();
                pc.addIceCandidate(
                    new RTCIceCandidate(data.candidate)
                );
            });

            channel.bind('client-onsdp', (data) => {
                console.log("onsdp");
                go();
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
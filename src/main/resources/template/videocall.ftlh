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
    {{ parent() }}
    <script src="https://js.pusher.com/4.1/pusher.min.js"></script>
    <script>
        Object.defineProperty(HTMLMediaElement.prototype, 'playing', {
            get: function(){
                return !!(this.currentTime > 0 && !this.paused && !this.ended && this.readyState > 2);
            }
        })

        function go() {
            document.getElementById("cover").remove();

        }

        function tryPlay() {
            let self = document.getElementById("selfview");
            let remote = document.getElementById("remoteview");
            self.play();
            remote.play();
            if (!self.playing || !remote.playing) {
                setTimeout(tryPlay, 200);
            }
        }
        tryPlay();

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
        var id, sessionDesc, room, caller, localUserMedia;
        const channel = pusher.subscribe(getUrlParameter("room"));

        channel.bind('pusher:subscription_succeeded', (members) => {
            id = channel.members.me.id;
            console.log("SUCCESS: " + members.count);
        });

        channel.bind('pusher:member_added', (member) => {
            console.log("ADDED: " + member.id);
            setTimeout(() => {
                callUser(member.id);
                go();
            }, 200);
        });

        channel.bind('pusher:member_removed', (member) => {
            console.log("removed");
            // // for remove member from list:
            // var index = users.indexOf(member.id);
            // users.splice(index, 1);
            // endCall();
            // render();
        });

        function render() {
            var list = '';
            users.forEach(function(user) {
                list += `<li>` + user + ` <input type="button" style="float:right;"  value="Call" onclick="callUser('` + user + `')" id="makeCall" /></li>`
            })
            document.getElementById('users').innerHTML = list;
        }

        //To iron over browser implementation anomalies like prefixes
        GetRTCPeerConnection();
        GetRTCSessionDescription();
        GetRTCIceCandidate();
        prepareCaller();
        function prepareCaller(){
            //Initializing a peer connection
            caller = new window.RTCPeerConnection();
            //Listen for ICE Candidates and send them to remote peers
            caller.onicecandidate = function(evt) {
                if (!evt.candidate) return;
                console.log("onicecandidate called");
                onIceCandidate(caller, evt);
            };
            //onaddstream handler to receive remote feed and show in remoteview video element
            caller.onaddstream = function(evt) {
                console.log("onaddstream called");
                if (window.URL) {
                    document.getElementById("remoteview").srcObject = evt.stream;
                } else {
                    document.getElementById("remoteview").src = evt.stream;
                }
            };
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

        //Create and send offer to remote peer on button click
        function callUser(user) {

            getCam()
                .then(stream => {
                    if (window.URL) {
                        document.getElementById("selfview").srcObject = stream;
                    } else {
                        document.getElementById("selfview").src = stream;
                    }
                    caller.addStream(stream);
                    localUserMedia = stream;
                    caller.createOffer().then(function(desc) {
                        caller.setLocalDescription(new RTCSessionDescription(desc));
                        channel.trigger("client-sdp", {
                            "sdp": desc,
                            "room": user,
                            "from": id
                        });
                        room = user;
                    });

                })
                .catch(error => {
                    console.log('an error occured', error);
                })
        };

        function endCall(){
            room = undefined;
            caller.close();
            for (let track of localUserMedia.getTracks()) { track.stop() }
            prepareCaller();
        }

        function endCurrentCall(){

            channel.trigger("client-endcall", {
                "room": room
            });

            endCall();
        }

        //Send the ICE Candidate to the remote peer
        function onIceCandidate(peer, evt) {
            if (evt.candidate) {
                channel.trigger("client-candidate", {
                    "candidate": evt.candidate,
                    "room": room
                });
            }
        }

        //Listening for the candidate message from a peer sent from onicecandidate handler
        channel.bind("client-candidate", function(msg) {
            if(msg.room==room){
                console.log("candidate received");
                caller.addIceCandidate(new RTCIceCandidate(msg.candidate));
            }
        });

        //Listening for Session Description Protocol message with session details from remote peer
        channel.bind("client-sdp", function(msg) {
            if(msg.room == id){
                console.log("sdp received");
                go();
                room = msg.room;
                var sessionDesc = new RTCSessionDescription(msg.sdp);
                caller.setRemoteDescription(sessionDesc).then(function() {
                    caller.createAnswer().then(function(sdp) {
                        caller.setLocalDescription(new RTCSessionDescription(sdp));
                        channel.trigger("client-answer", {
                            "sdp": sdp,
                            "room": room
                        });
                    });
                });
                getCam()
                    .then(stream => {
                        localUserMedia = stream;
                        if (window.URL) {
                            document.getElementById("selfview").srcObject = stream;
                        } else {
                            document.getElementById("selfview").src = stream;
                        }
                        caller.addStream(stream);
                    })
                    .catch(error => {
                        console.log('an error occured', error);
                    })
            }


        });

        //Listening for answer to offer sent to remote peer
        channel.bind("client-answer", function(answer) {
            if(answer.room==room){
                console.log("answer received");
                caller.setRemoteDescription(new RTCSessionDescription(answer.sdp));
            }

        });

        channel.bind("client-reject", function(answer) {
            if(answer.room==room){
                console.log("Call declined");
                alert("call to " + answer.rejected + "was politely declined");
                endCall();
            }

        });

        channel.bind("client-endcall", function(answer) {
            if(answer.room==room){
                console.log("Call Ended");
                endCall();

            }

        });
    </script>
</#macro>

<@display_page/>
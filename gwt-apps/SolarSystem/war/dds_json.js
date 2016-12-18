
function getReadURI() {

   var uri = "http://" + window.location.hostname + 
      ":8080/dds/webApp/*/Planet/DDSSolarSystem.Planet/subscribe/webReader/read";
   
   req = false;
   
   // branch for native XMLHttpRequest object
   if(window.XMLHttpRequest) {
      try {
         req = new XMLHttpRequest();
      } catch(e) {
         req = false;
      }
   // branch for IE/Windows ActiveX version
   } else if (window.ActiveXObject) {
      try {
         req = new ActiveXObject("Msxml2.XMLHTTP");
      } catch(e) {
         try {
            req = new ActiveXObject("Microsoft.XMLHTTP");
         } catch(e) {
            req = false;
         }
      }
   }
   
   if(req) {
       req.onreadystatechange = function() { processReadStateChange(req); };
       req.open("GET", uri, true);
       req.send(null);
    }
}

function processReadStateChange(req) {

    // only if req shows "loaded"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
           processReadJSON(req);
        } else {
           alert("DDS JSON Error: " + req.responseText);
        }
    }
}

function processReadJSON(req) {

    updatePlanetFields(req.responseText);
    
    if (document.getElementById('autoread').checked)
       setTimeout(function() { getReadURI(); }, 1000);
}

function updatePlanetFields(responseText) {       
 
    var obj = eval ( "(" + responseText + ")" );
    var sample = parseInt(document.getElementById('sample').value);
    document.getElementById('planetID').value = obj.samples[sample].data.planetId.toString();
    document.getElementById('planetName').value = obj.samples[sample].data.planetName;
    document.getElementById('planetColor').value = obj.samples[sample].data.planetColor;
    document.getElementById('xPos').value = obj.samples[sample].data.xPos.toString();
    document.getElementById('yPos').value = obj.samples[sample].data.yPos.toString();
    document.getElementById('theta').value = obj.samples[sample].data.theta.toString();
    document.getElementById('planetSize').value = obj.samples[sample].data.planetSize.toString();
    document.getElementById('orbitalRadius').value = obj.samples[sample].data.orbitalRadius.toString();
    document.getElementById('orbitalVelocity').value = obj.samples[sample].data.orbitalVelocity.toString();
       
    document.getElementById('dds_json_data').value = responseText;
}

function numReplacer(key, value) {

    if (typeof value === 'number' && !isFinite(value)) {
        return String(value);
    }
    return value;
}

function postWriteURI() {
        
   var uri = "http://" + window.location.hostname + 
        ":8080/dds/webApp/*/Planet/DDSSolarSystem.Planet/publish/webWriter/write";
        
   var theJSObject = new Array();
   
   theJSObject = {"value":[
      {"planetId":0, "planetName":"","planetColor":"", "xPos":0, "yPos":0, "theta":0, "planetSize":0, "orbitalRadius":0, "orbitalVelocity":0}
   ]};

   theJSObject.value[0].planetId = parseInt(document.getElementById('planetID').value);
   theJSObject.value[0].planetName = document.getElementById('planetName').value;
   theJSObject.value[0].planetColor = document.getElementById('planetColor').value;
   theJSObject.value[0].xPos = parseFloat(document.getElementById('xPos').value);
   theJSObject.value[0].yPos = parseFloat(document.getElementById('yPos').value);
   theJSObject.value[0].theta = parseFloat(document.getElementById('theta').value);
   theJSObject.value[0].planetSize = parseFloat(document.getElementById('planetSize').value);
   theJSObject.value[0].orbitalRadius = parseFloat(document.getElementById('orbitalRadius').value);
   theJSObject.value[0].orbitalVelocity = parseFloat(document.getElementById('orbitalVelocity').value);
   
   var theJSONData = JSON.stringify(theJSObject, numReplacer);
   httpPost(uri, "samples=" + theJSONData);
}

function httpPost(uri, data) {
    
   var req = false;
   
   // branch for native XMLHttpRequest object
   if (window.XMLHttpRequest) {
      try {
         req = new XMLHttpRequest();
      } catch(e) {
         req = false;
      }
   }
   // branch for IE/Windows ActiveX version
   else if (window.ActiveXObject) {
      try {
         req = new ActiveXObject("Msxml2.XMLHTTP");
      } catch(e) {
         try {
            req = new ActiveXObject("Microsoft.XMLHTTP");
         } catch(e) {
            req = false;
         }
      }        
   }
   
   if (req) {
       req.open("POST", uri, true);
       req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
       req.onreadystatechange = function() {
           if (req.readyState == 4) {
           }
       }
   }
   req.send(data);
}


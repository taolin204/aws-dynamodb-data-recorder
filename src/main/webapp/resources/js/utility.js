/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var drawSVG ;
var drawMainSvG;

function getContextPath() {
    return '/' + window.location.pathname.split('/')[1] + '/';
}

function verifyAction(action, node, label, checkSysNode) {
    var domNode = document.getElementById(PF(node).id + '_input');
    if (domNode.selectedOptions[0].value == 0)
    {
        alert("Cannot " + action + ", invalid " + label + " '" + domNode.selectedOptions[0].label + "' selected!");
        return false;

    } else if (action == "delete") {
        if (checkSysNode != "")
        {
            var domNodeSys = document.getElementById(checkSysNode);
            if (domNodeSys.checked)
            {
                alert("Cannot " + action + ", " + label + " '" + domNode.selectedOptions[0].label + "' is system defined!");
                return false;
            }
        }

        return confirm("Delete " + label + " '" + domNode.selectedOptions[0].label + "', confirm?")
    }
    return true;
}

function verifyButtonAction(action, node, label, checkSysNode) {
    var domNode = document.getElementById(PF(node).id + '_input');
    if (domNode.value == 0)
    {
        alert("Cannot " + action + ", invalid " + label + " '" + domNode.value + "' selected!");
        return false;

    } else if (action == "delete") {
        if (checkSysNode != "")
        {
            var domNodeSys = document.getElementById(checkSysNode);
            if (domNodeSys.checked)
            {
                alert("Cannot " + action + ", " + label + " '" + domNode.selectedOptions[0].label + "' is system defined!");
                return false;
            }
        }

        return confirm("Delete " + label + " '" + domNode.selectedOptions[0].label + "', confirm?")
    }
    return true;
}

function CompareWithCurrDate(action, label, node)
{

    var currDate = new Date();
    currDate.setHours(0);
    currDate.setMinutes(0);
    currDate.setSeconds(0);
    currDate.setMilliseconds(0);


    var strDate = PF(node).getDate();
    strDate.setHours(0);
    strDate.setMinutes(0);
    strDate.setSeconds(0);
    strDate.setMilliseconds(0);


    if (strDate >= currDate) {
        return true;
    } else {
        alert("Cannot " + action + "," + label + " cannot be less than Current date!")
        return false;
    }
}


function verifyTxtFldReq(action, node, label) {
    var domNodeValue = PF(node).getValue();
    if (domNodeValue == null || domNodeValue == "" || domNodeValue == 0)
    {
        alert("Cannot " + action + ", " + label + " is blank!");
        return false;

    } else if (action == "delete") {

        return confirm("Delete " + label + " '" + domNodeValue + "', confirm?");
    }
    return true;
}

function passwordMatch(newpwNode, confpwNode) {
    var newPw = document.getElementById(PF(newpwNode).id).value;
    var confPw = document.getElementById(PF(confpwNode).id).value;
    if (newPw != confPw) {
        alert("Confirm password should be the same as new password!");
        return false;
    }
    return true;
}
function resetField() {
    for (var i = 0; i < arguments.length; i++) {
        PF(arguments[i]).setValue(0);
    }
}

function calculateTotal(quantity, price, totalAmt) {
    var quan = PF(quantity).getValue();
    var rate = PF(price).getValue();
    var total = parseFloat(quan * rate);
    PF(totalAmt).setValue(total.toFixed(2));
}

///**
// * Faces Validator
// */
//PrimeFaces.validator['custom.TextField'] = {
//    validate: function (element, value) {
//        //use element.data() to access validation metadata, in this case there is none.
//        if (value == null || value.trim() == '') {
//            throw {
//                summary: 'Validation Error',
//                detail: value + ' is not a valid email.'
//            }
//        }
//    }
//};

/**
 * Load SVG map from URL.
 */
function loadSVG(container, url) {
    $.get(url, function (data,status,xhr) {
        console.log("Status "+status);
        container.empty();
        drawSVG = SVG(container[0].id).size(window.innerWidth, window.innerHeight);
        drawSVG.svg(new XMLSerializer().serializeToString(data.documentElement));
        drawSVG.style({cursor: 'all-scroll'});
        drawMainSvG = SVG.get("svg2");
        var beforePan;

        beforePan = function(oldPan, newPan){
          var stopHorizontal = false
            , stopVertical = false
            , gutterWidth = (panZoom.getSizes().viewBox.width * panZoom.getSizes().realZoom)
            , gutterHeight = (panZoom.getSizes().viewBox.height * panZoom.getSizes().realZoom)
              // Computed variables
            , sizes = this.getSizes()
            , leftLimit = -((sizes.viewBox.x + sizes.viewBox.width) * sizes.realZoom) + gutterWidth
            , rightLimit = sizes.width - gutterWidth - (sizes.viewBox.x * sizes.realZoom)
            , topLimit = -((sizes.viewBox.y + sizes.viewBox.height) * sizes.realZoom) + gutterHeight
            , bottomLimit = sizes.height - gutterHeight - (sizes.viewBox.y * sizes.realZoom)

          customPan = {}
          customPan.x = Math.max(leftLimit, Math.min(rightLimit, newPan.x))
          customPan.y = Math.max(topLimit, Math.min(bottomLimit, newPan.y))

          return customPan
        }
        window.panZoom = svgPanZoom('#' + drawSVG.node.id, {
            zoomEnabled: true,
            panEnabled: true,
            contain: true,
            controlIconsEnabled: false,
            fit: true,
            center: false,
            minZoom: 0.1,
            maxZoom: 5
//            ,onZoom: onZoonSvg
//            , beforePan: beforePan
        });
//
//        // zoom out
//        window.panZoom.zoomAtPointBy(2, {x: 1200, y: 300})
            window.panZoom.pan({x:0,y:70});
            var realZoom= window.panZoom.getSizes().realZoom;
//            window.panZoom.pan({  
//                        x: -(1600*realZoom)+(window.panZoom.getSizes().width/2),
//                        y: -(2900*realZoom)+(window.panZoom.getSizes().height/2)
//                        });
//            window.panZoom.pan({x: 1200, y: 300});
//              window.panZoom.zoomAtPointBy(5, {  
//                        x: -(1000*realZoom)+(window.panZoom.getSizes().width/2),
//                        y: -(3500.3384*realZoom)+(window.panZoom.getSizes().height/2)
//                        });
        window.panZoom.zoom(1.2);
    });
}


 function loadPicLocation(){
   document.getElementById('mainpageForm:eventType').value = "loadPicLocation"; 
   refreshDetails();
 }
 
 
 function createPICLocation(container, tackRect){
//     var trackCk = tackRect.getBoundingClientRect();
//    var iDiv = document.createElement('div');
//    iDiv.id = 'block';
//    iDiv.style.position = "absolute";
//    iDiv.style.left = window.scrollX+trackCk.left+'px';
//    iDiv.style.top = window.scrollY+trackCk.top+'px';
//    iDiv.className = 'trackLocation';
//    container[0].appendChild(iDiv);     
// do something with it, e.g. draw a reactangle around (with jQuery here)
 }
 
function createPicNode(trackId, textLable) {
    var trackNode = SVG.get(trackId);
    var xCor = trackNode.x();
    var yCor = trackNode.y();
    var width = trackNode.width();
    var height = trackNode.height();
    drawMainSvG = SVG.get("Mainline");
    //create group
    var group = drawMainSvG.group();
    group.id("gPicNode"+trackId);
    var text = drawMainSvG.text(textLable);
    text.font({ fill: '#ffffff', family: 'Arial', weight: 'bold', size:'70'});
    //add text to group
    group.add(text);
    var image = drawMainSvG.image('../../../tass/resources/image/worker.png');
    image.size(200, 155);
    image.style('cursor: crosshair; border-top-left-radius: 165px; border-top-right-radius: 165px;');
//    image.move(xCor + (width / 2)-100, yCor-155);
    image.on('click', picLocationClick);
//    image.on('mouseover', ShowTooltip);
//    image.on('mouseout', HideTooltip);
    group.add(image);
    group.translate(xCor + (width / 2)-100, yCor-155);
    text.dmove(10, -140);
}
 
// // Pan when mouse is dragged
//$(window).on('mousedown', function(event){
//    var initClick = {
//        x : event.pageX,
//        y : event.pageY
//    };
//    var initPosition = $('#block').offset();
//    var handlers = {
//        mousemove : function(event){
//            window.panZoom.pan({x: event.pageX - initClick.x, y: event.pageY - initClick.y});
//            $('#block').css({"top": initPosition.top + (event.pageY - initClick.y),
//                            "left": initPosition.left + (event.pageX - initClick.x)});
//        },
//        mouseup : function(event){
//            $(this).off(handlers);   
//        }
//    };
//    $(document).on(handlers);
//});

var picLocationClick = function(event) {
//    console.log(event);
//    alert(event);
        PF('toolTipDlg').show()
}

function ShowTooltip(event) {
     var toolText = drawMainSvG.text("ToolTip");
     toolText.attr(id,"tootip");
    var point = toolText.point(event.screenX, event.screenY);
     toolText.move(point.x, point.y);
     toolText.font({ fill: '#ffffff', family: 'Arial', weight: 'bold', size:'70'});
     toolText.show();
//    var tooltip = document.getElementById('tooltip');
//    tooltip.setAttribute("x", xCor + 11);
//    tooltip.setAttribute("y", yCor + 27);
//    tooltip.innerHTML = mouseovertext;
//    tooltip.setAttribute("visibility", "visible");
}

function HideTooltip(event) {
    SVG.get("tootip");
    var toolText = toolText.hide();
//    var tooltip = document.getElementById('tooltip');
//    tooltip.setAttribute("visibility", "hidden");
}

function handleEventNotification(data){
//    document.getElementById('mainpageForm:eventType').value = data; 
//    refreshDetails();
      var parsedMessage = JSON.parse(data);
      switch(parsedMessage.messageType){
          case 'LocationUpdate':
            locatePic(parsedMessage.data);
            break;
            default : 
            break;
      }
}


function locatePic(userLocationData) {
    console.log(userLocationData);
    var xCor = userLocationData.xCoordinate;
    var yCor = userLocationData.yCoordinate;
    var width = 0;
    var group = SVG.get("gPicNode_"+userLocationData.userDeviceId);
    if(userLocationData.locationStatus === 'EXITED_DOOR'
            && group !== null ){
        group.remove();
    }
    if( group === null){        
            //create group
        group = drawMainSvG.group();
        group.id("gPicNode_"+userLocationData.userDeviceId);
        //PIC Lable
        var text = drawMainSvG.text(userLocationData.picName);
        text.font({ fill: '#000000', family: 'Arial', weight: 'bold', size:'30'});
        //add text to group
        group.add(text);
        //PIC ICON
        var image = drawMainSvG.image('../../../tass/faces/resources/image/worker.png');
        image.id("picImage_"+userLocationData.userDeviceId);
        image.size(96, 74);
        image.style('cursor: crosshair; border-top-left-radius: 165px; border-top-right-radius: 165px;');
    //    image.move(xCor + (width / 2)-100, yCor-155);
//        image.on('click', picLocationClick);
//        image.on('mouseover', ShowTooltip);
    //    image.on('mouseout', HideTooltip);
        group.add(image);
        //Location POINT
//        var point = drawMainSvG.circle(20).fill('red');        
        var point = drawMainSvG.circle(20);//blue circle
        point.style({fill: 'none', stroke: '#0084ff','stroke-width': '1px'});
        var innerPoint = drawMainSvG.circle(10).fill('#0084ff');//blue dot
        group.add(point);
        group.add(innerPoint);
        
        point.dmove(38,64);
        innerPoint.dmove(43,69);
        text.dmove(18, -52);
    }
    group.translate(xCor + (width / 2)-48, yCor-74);
   
}

beforeZoomSvg = function(oldZoom, newZoom){
    var group = SVG.get("gPicNode_Device-1");
    if(picImage !== null){
        picImage.size(40, 37);
    }
}

onZoomSvg = function (event){
    var picImage = SVG.get("device1");
    if(picImage !== null){
        picImage.size(40, 37);
    }
}

/**
 *  Send ajax request
 * @param {type} urlString
 * @param {type} httpMethod
 * @param {type} dataType
 * @param {type} data
 * @param {type} successCallback
 * @param {type} errorCallback
 * @returns {undefined}
 */
function sendRestRequest(urlString, httpMethod, contentType, dataType, data, successCallback, errorCallback){
    $.ajax({
        url: urlString.replace("/faces",""),
        type: httpMethod,
        contentType: contentType,
        dataType: dataType,
        data: data,
        success: successCallback,
        error: errorCallback 
    });
}

function locationData(data,status,xhr){
    console.log(status);
    console.log(data);
    var picLocationList = JSON.parse(data.picLocationList);
     for (var i = 0; i < picLocationList.length; i++) {
        locatePic(picLocationList[i]);
    }
}


function errorProcess(qXHR, textStatus, errorThrown ){
     console.log(textStatus);
    console.log(errorThrown);    
}


function drawSectorPolygon(data,status,xhr){
    console.log(status);
    console.log(data);
    var workSectorData = data.workSectorData;
     for (var i = 0; i < workSectorData.length; i++) {
         var polygon = drawMainSvG.polygon(workSectorData[i].polyPoints);
            polygon.fill({color: workSectorData[i].colorCode, opacity: 0.3 });
            polygon.style({stroke: workSectorData[i].colorCode.substr(0,4),'stroke-width': '1px'});            
    }
}
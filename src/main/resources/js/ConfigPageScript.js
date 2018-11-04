
var url = AJS.contextPath() + "/rest/aliasconfig/1.0/";

(function ($) {
    $(document).ready(function() {
        $.ajax({
            url: url + "aliases",
            dataType: "json"
        }).done(function (data) {
            $.each(data, function(i, item) {
                addAliasRow(item.alias, item.text, item.id);
            });
        });
    });
})(AJS.$ || jQuery)

function deleteAlias(id) {
    $.ajax({
        type: "DELETE",
        url: url + "aliase/" + id,
        success: function(msg){
            //if (msg == "ok")
            $('#r' + id).remove();
        }
    });
}

function addAlias() {
    var aliasValue = document.getElementById("newAlias").value;
    var textValue  = document.getElementById("newText").value;
    $.ajax({
        type: "POST",
        url: url + "aliases",
        contentType: "application/json",
        data: '{ "alias": "' + aliasValue + '", "text": "' + textValue + '", "id": "0" }',
        success: function(id){
            if (id.includes("Error")) {
                alert(id);
                return;
            }
            addAliasRow(aliasValue, textValue, id);
            document.getElementById("newAlias").value = "";
            document.getElementById("newText").value = "";
        }
    });
}

function addAliasRow(alias, text, id) {
    var deletingElement = "<a onclick='deleteAlias(" +id +")' style='color: red'>X</a>";
    $("#aliases tr:last").after("<tr id='r" + id + "'><td>"+ alias + "&nbsp;&nbsp;&nbsp;</td><td>" + text +"</td><td>" + deletingElement + "</td></tr>");
}
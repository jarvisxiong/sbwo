sap.ui.define([
	"sap/ui/model/type/DateTime"
], function(DateTime){
	
	return DateTime.extend("spet.sbwo.web.util.Timestamp", {
		
		constructor: function() {
			DateTime.apply(this, arguments);
		},
		
		formatValue: function(iValue, sType) {
			sType = this.getPrimitiveType(sType);
			if (iValue === null || iValue === undefined) {
				return iValue;
			}
			
			if (sType === "int") {
				return iValue;
			} 
			else if (sType === "object") {
				return new Date(iValue);
			}
			else {
				return DateTime.prototype.formatValue.call(this, new Date(iValue), sType);
			}
		},
		
		parseValue: function(oValue, sType) {
			sType = this.getPrimitiveType(sType);
			if (oValue === null || oValue === undefined) {
				return oValue;
			}
			
			if (sType === "int") {
				return oValue;
			}
			else if (sType === "object") {
				return oValue.getTime();
			}
			else {
				return DateTime.prototype.parseValue.call(this, oValue, sType).getTime();
			}
		},
		
		validateValue: function() {
			return true;
		}
	});
	
});

// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

// requires:
// rating - server_id
Parse.Cloud.define("showRating", function(request, response) {
	var rating1 = request.params["rating"];
  
	var Show = Parse.Object.extend("Show");
	var query = new Parse.Query(Show);
	query.get(request.params["server_id"], {
	  success: function(show) {
		// The object was retrieved successfully.
		var rating_count = show.get("rating_count");
		var rating = show.get("rating");
		
		show.increment("rating_count");
		show.set("rating", (rating + rating1) / (rating_count + 1));
		
		show.save(null, {
			success: function(gameScore) {
			}
		});
		
		response.success();
	  },
	  error: function(object, error) {
		// The object was not retrieved successfully.
		// error is a Parse.Error with an error code and message.
		response.error("show not found");
	  }
	});
	
	
  
	
});

// requires:
// rating - server_id
Parse.Cloud.define("channelRating", function(request, response) {
	var rating1 = request.params["rating"];
  
	var Show = Parse.Object.extend("Channel");
	var query = new Parse.Query(Show);
	query.get(request.params["server_id"], {
	  success: function(show) {
		// The object was retrieved successfully.
		var rating_count = show.get("rating_count");
		var rating = show.get("rating");
		
		show.increment("rating_count");
		show.set("rating", (rating + rating1) / (rating_count + 1));
		
		show.save(null, {
			success: function(gameScore) {
			}
		});
		
		response.success();
	  },
	  error: function(object, error) {
		// The object was not retrieved successfully.
		// error is a Parse.Error with an error code and message.
		response.error("channel not found");
	  }
	});
	
	
  
	
});
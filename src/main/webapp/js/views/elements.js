define([
	'underscore',
	'mainView',
	'collections/elements',
	'text!templates/elements.html'
	], function(_, MainView, Elements,	templateSource) {
	var ElementsView = MainView.extend({

		// compile template
		template : _.template(templateSource),

		initialize : function(option) {
			this.constructor.__super__.initialize.apply(this, [ option ]);
			_.bindAll(this, 'render');

			$(document).bind('CloseView', this.close);
			
			Elements.fetch({
				success : this.render
			});
		},

		render : function(model) {
			(this.el).html(this.template({
				elements : model.toJSON()
			}));
		},
		close : function() {
			$(this).unbind();
			$("#main").hide();
		}

	});
	return ElementsView;
});

window.onload = function(){
	tinymce.init({
		selector:"#content",
		plugins: 'advlist autolink link lists charmap print preview emoticons textcolor',
		toolbar:[
			'undo redo | styleselect | bold italic | link | alignleft aligncenter alignright | forecolor backcolor emoticons'
		],
		height:400,
		menubar:false
	});
}
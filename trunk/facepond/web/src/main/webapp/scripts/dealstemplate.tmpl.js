<script id="CouponDisplayTemplate" type="text/x-jQuery-tmpl">
	<div class="discussionItem">
		<h5 class="talkboxImage" style="background: url(${mediumImageUrl}?size=50) no-repeat;">			
		</h5>
		<div class="discussionItemContent">
			<h4><span id="${dealUrl}" class="viewDetailLink">${announcementTitle}</span></h4>
			<div class="description"><p>${title}</p></div>
			<dl class="discussionItemDetails">
				Merchant Name: ${merchant.name} - <a href="#" id="${merchant.websiteUrl}" class="viewDetailLink"> ${merchant.websiteUrl}</a>
			</dl>
			<div class="discussionItemTools clearthis">
				<dl class="createdDateTime">
					<dt>EndDate:</dt>
					<dd class="createdDate">${endAt}</dd>
				</dl>
			</div>
			{{if options}}
				{{tmpl "#DiscountTemplate"}}
			{{/if}}
			
		</div>
	</div>
</script>
<script id="DiscountTemplate" type="text/x-jQuery-tmpl">

<div class="item_comment_description">
	<ul class="clearthis">
		<li><strong>Value ${actualprice}</strong></li>
		<li><strong>Discount ${discountPercent}%</strong></li>
		<li><strong>Price ${disprice}</strong></li>
	</ul>
</div>
</div>
</script>
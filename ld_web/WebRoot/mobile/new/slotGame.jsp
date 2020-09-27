<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="最新优惠" />
		</jsp:include>
		<link rel="stylesheet" href="/mobile/css/loader.css">
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
		<link rel="stylesheet" href="/mobile/css/loader.css">
		<style>
			.layui-m-layercont {
				padding: 0.666666rem 0.4rem;
			}
		</style>
	</head>

	<body>
		<title>天威</title>
		<div class="slot-filter">
			<div id="j-searchForm" class="game-top-bar">
				<span id="j-come-back">
					<span class="back-gameindex j-back-gameindex" id="backApp">
						<i class="iconfont icon-arrow-left"></i>返回
					</span>
				</span>
				<div class="search-box">
					<input class="j-ipt" type="text" placeholder="关键词搜索">
					<div class="select-list j-select"> </div>
				</div>
				<button id='filter_dropdown_trigger' class="icon_search0 fl j-btnSearch">
					<img src="/mobile/img/icon/filter2.png" alt=""/>		
				</button>
				<a class="shaixuan" href="/mobile/new/history.jsp">
					<img src="/mobile/img/icon/history.png" alt="" />
				</a>
			</div>
		</div>

		<ul class="slatform-screening">
			<li class="menu_item active" data-type="slot">老虎机</li>
			<li class="menu_item">真人</li>
			<li class="menu_item">捕鱼</li>
			<li class="menu_item">体育</li>
			<li class="menu_item">棋牌</li>
		</ul>
		<div id="j-filter" class="slot-item-warp">

			<div class="slot-item-list item-game  active">
				<div class="filter_dropdown_content_sec">
					<h3 class="section-title"><span>游戏平台</span></h3>
					<ul class="filter_item_content" id="filterUl">
						<%--<li class="filter_item active">--%>
						<%--<a data-tab="ALL" data-value='' href="javascript:;">全选</a>--%>
						<%--</li>--%>
						<li class="filter_item active">
							<a data-tab="PT" data-value='{"category":"PT"}' href="javascript:;">PT</a>
						</li>
						<li class="filter_item">
							<a data-tab="PTSW" data-value='{"category":"PTSW"}' href="javascript:;">SW</a>
						</li>
						<li class="filter_item">
							<a data-tab="DT" data-value='{"category":"DT"}' href="javascript:;">DT</a>
						</li>
						<li class="filter_item">
							<a data-tab="MGS" data-value='{"category":"MGS"}' href="javascript:;">MG</a>
						</li>
						<li class="filter_item">
							<a data-tab=PNG data-value='{"category":"PNG"}' href="javascript:;">PNG</a>
						</li>
						<li class="filter_item">
							<a data-tab="QT" data-value='{"category":"QT"}' href="javascript:;">QT</a>
						</li>
						<li class="filter_item">
							<a data-tab="NT" data-value='{"category":"NT"}' href="javascript:;">NT</a>
						</li>
						<li class="filter_item">
							<a data-tab="TTG" data-value='{"category":"TTG"}'>TTG</a>
						</li>
						<li class="filter_item">
							<a data-tab="AG" data-value='{"category":"AG"}' href="javascript:;">AG</a>
						</li>
					</ul>
				</div>

				<div id="PT" class="item active">
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="mobile_sec">
						<h3 class="section-title"><span>类型</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"NEW"}'>新游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"HOT"}'>热门游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"CLA"}'>经典老虎</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"SLO"}'>电动吃角子</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"AMA"}'>奖池游戏</a>
							</li>

						</ul>
					</div>
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>赔付线</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"1-1"}'>单线</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"5-10"}'>5-10</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"15-20"}'>15-20</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"25-25"}'>25+</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"243"}'>243游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"99-99"}'>其他类</a>
							</li>
						</ul>

					</div>

					<%--<div class="space_border_wrap"><div class="space_border"></div></div>--%>
					<%--<div class="filter_dropdown_content_sec">--%>
					<%--<h3>游戏风格</h3>--%>
					<%--<ul class="filter_item_content">--%>
					<%--<li class="filter_item active">--%>
					<%--<a href="javascript:;" data-value=''>全选</a>--%>
					<%--</li>--%>
					<%--<li class="filter_item">--%>
					<%--<a href="javascript:;" data-value='{"tag":"CAR"}'>卡通</a>--%>
					<%--</li>--%>
					<%--<li class="filter_item">--%>
					<%--<a href="javascript:;" data-value='{"tag":"MOV"}'>电影</a>--%>
					<%--</li>--%>
					<%--<li class="filter_item">--%>
					<%--<a href="javascript:;" data-value='{"tag":"GIR"}'>少女</a>--%>
					<%--</li>--%>
					<%--</ul>--%>

					<%--</div>--%>
				</div>

				<div id="PTSW" class="item">
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>类型</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"NEW"}'>新游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"HOT"}'>热门游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"CLA"}'>经典老虎</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"SLO"}'>电动吃角子</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"AMA"}'>奖池游戏</a>
							</li>

						</ul>
					</div>
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>赔付线</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"1-1"}'>单线</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"5-10"}'>5-10</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"15-20"}'>15-20</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"25-25"}'>25+</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"243"}'>243游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"99-99"}'>其他类</a>
							</li>
						</ul>

					</div>

				</div>

				<div id="DT" class="item">
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>类型</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"NEW"}'>新游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"HOT"}'>热门游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"CLA"}'>经典老虎</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"SLO"}'>电动吃角子</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"AMA"}'>奖池游戏</a>
							</li>

						</ul>
					</div>
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>赔付线</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"1-1"}'>单线</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"5-10"}'>5-10</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"15-20"}'>15-20</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"25-25"}'>25+</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"243"}'>243游戏</a>
							</li>
							<%--<li class="filter_item">--%>
							<%--<a href="javascript:;" data-value='{"line":"99-99"}'>其他类</a>--%>
							<%--</li>--%>
						</ul>

					</div>
				</div>

				<div id="MGS" class="item">
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>类型</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"NEW"}'>新游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"HOT"}'>热门游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"CLA"}'>经典老虎</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"SLO"}'>电动吃角子</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"AMA"}'>奖池游戏</a>
							</li>

						</ul>
					</div>
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>赔付线</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"1-1"}'>单线</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"5-10"}'>5-10</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"15-20"}'>15-20</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"25-25"}'>25+</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"243"}'>243游戏</a>
							</li>
						</ul>

					</div>
				</div>

				<div id="PNG" class="item">
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>类型</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"NEW"}'>新游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"HOT"}'>热门游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"CLA"}'>经典老虎</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"AMA"}'>奖池游戏</a>
							</li>

						</ul>
					</div>
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>赔付线</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"1-1"}'>单线</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"5-10"}'>5-10</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"15-20"}'>15-20</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"25-25"}'>25+</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"243"}'>243游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"99-99"}'>其他类</a>
							</li>
						</ul>

					</div>
				</div>

				<div id="QT" class="item">
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>类型</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"NEW"}'>新游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"HOT"}'>热门游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"SLO"}'>电动吃角子</a>
							</li>

						</ul>
					</div>
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>赔付线</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"5-10"}'>5-10</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"15-20"}'>15-20</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"25-25"}'>25+</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"243"}'>243游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"99-99"}'>其他类</a>
							</li>
						</ul>

					</div>
				</div>

				<div id="NT" class="item">
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>类型</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"NEW"}'>新游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"HOT"}'>热门游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"SLO"}'>电动吃角子</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"MIN"}'>迷你游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"AMA"}'>奖池游戏</a>
							</li>

						</ul>
					</div>
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>赔付线</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"5-10"}'>5-10</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"15-20"}'>15-20</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"25-25"}'>25+</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"243"}'>243游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"99-99"}'>其他类</a>
							</li>
						</ul>

					</div>
				</div>
				<div id="TTG" class="item">
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>类型</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"NEW"}'>新游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"HOT"}'>热门游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"SLO"}'>经典游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"BMG"}'>Blooming游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"PMT"}'>Pragmatic游戏</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"tag":"PSG"}'>Playson游戏</a>
							</li>
						</ul>
					</div>
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>赔付线</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"1-1"}'>单线</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"5-10"}'>5-10</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"15-20"}'>15-20</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"25-25"}'>25+</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"243"}'>243游戏</a>
							</li>
						</ul>

					</div>
				</div>

				<div id="AG" class="item">
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>类型</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a data-value='{"tag":"RQ"}' href="javascript:;">热门</a>
							</li>
							<li class="filter_item">
								<a data-value='{"tag":"CF"}' href="javascript:;">中奖率排行</a>
							</li>
							<li class="filter_item">
								<a data-value='{"tag":"LS"}' href="javascript:;">流水排行</a>
							</li>

						</ul>
					</div>
					<div class="space_border_wrap">
						<div class="space_border"></div>
					</div>
					<div class="filter_dropdown_content_sec">
						<h3 class="section-title"><span>赔付线</span></h3>
						<ul class="filter_item_content">
							<li class="filter_item active">
								<a href="javascript:;" data-value=''>全选</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"5-10"}'>5-10</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"15-20"}'>15-20</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"25-25"}'>25-30</a>
							</li>
							<li class="filter_item">
								<a href="javascript:;" data-value='{"line":"99-99"}'>其他类</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="btn_wrap">
				<a id="j-resetBtn" href="javascript:;" class="btn btn07">重置</a>
				<a id="j-slideup" href="javascript:;" class="btn btn06">完成</a>
			</div>
		</div>
		<div class="dtJackpot">
			<img src="/mobile/img/jackport-txt.png" alt="" />
			<div id="j-jackpotCount">
				<span>4</span>
				<span>0</span>
				<span>,</span>
				<span>1</span>
				<span>9</span>
				<span>4</span>
				<span>,</span>
				<span>5</span>
				<span>6</span>
				<span>7</span>
				<span>.</span>
				<span>0</span>
				<span>4</span>
			</div>
		</div>
		<ul class="content-filter filter_item_content" id="filter-2" style="padding-top: 20px;">
			<li class="filter_item active">
				<a data-tab="PT" data-value='{"category":"PT"}' href="javascript:;">PT</a>
			</li>
			<li class="filter_item">
				<a data-tab="PTSW" data-value='{"category":"PTSW"}' href="javascript:;">SW</a>
			</li>
			<li class="filter_item">
				<a data-tab="DT" data-value='{"category":"DT"}' href="javascript:;">DT</a>
			</li>
			<li class="filter_item">
				<a data-tab="MGS" data-value='{"category":"MGS"}' href="javascript:;">MG</a>
			</li>
			<li class="filter_item">
				<a data-tab=PNG data-value='{"category":"PNG"}' href="javascript:;">PNG</a>
			</li>
			<li class="filter_item">
				<a data-tab="QT" data-value='{"category":"QT"}' href="javascript:;">QT</a>
			</li>
			<li class="filter_item">
				<a data-tab="NT" data-value='{"category":"NT"}' href="javascript:;">NT</a>
			</li>
			<li class="filter_item">
				<a data-tab="TTG" data-value='{"category":"TTG"}'>TTG</a>
			</li>
			<li class="filter_item">
				<a data-tab="AG" data-value='{"category":"AG"}' href="javascript:;">AG</a>
			</li>
		</ul> 
		<div class="slot_game_item_list active" id="j-gameContainer">

		</div>
		<div class="slot_game_item_list">
			<div class="live-item" id="aglivegame"><img src="/mobile/img/live_1.png" alt="" /></div>
			<div class="live-item" id="n2livegame"><img src="/mobile/img/live_2.png" alt="" /></div>
		</div>
		<div class="slot_game_item_list">
			<div class="slot_game_item game-info">
				<div class="layout_image_hover_text">
					<img class="game_img lazy" src="/mobile/img/fish_1.png"></div>
				<div class="game_item_operations">
					<div class="game-text">AG捕鱼</div>
					<div class="o-btn btn-play" id="agfishgame">立即游戏</div>
				</div>
			</div>
			<!--<div class="slot_game_item game-info">
				<div class="layout_image_hover_text">
					<img class="game_img lazy" src="/mobile/img/fish_2.png"></div>
				<div class="game_item_operations">
					<div class="game-text">AG网页捕鱼</div>
					<div class="o-btn btn-play" id='agfishweb'>立即游戏</div>
				</div>
			</div>-->
			<div class="slot_game_item game-info">
				<div class="layout_image_hover_text">
					<img class="game_img lazy" src="/mobile/img/fish_3.png"></div>
				<div class="game_item_operations">
					<div class="game-text">GG捕鱼</div>
					<div class="o-btn btn-play" id="ggfishweb">立即游戏</div>
				</div>
			</div>
			<div class="slot_game_item game-info">
				<div class="layout_image_hover_text">
					<img class="game_img lazy" src="/mobile/img/fish_4.png"></div>
				<div class="game_item_operations">
					<div class="game-text">MWG捕鱼</div>
					<div class="o-btn btn-play" id="mwgfishweb">立即游戏</div>
				</div>
			</div>
			<div class="slot_game_item game-info">
				<div class="layout_image_hover_text">
					<img class="game_img lazy" src="/mobile/img/fish_5.png"></div>
				<div class="game_item_operations tc">
					<div class="game-text">钱来捕鱼</div>
					<a class="o-btn btn-play" href="/game/gameLoginDTFish.aspx">立即游戏</a>
				</div>
			</div>
		</div>

		<div class="slot_game_item_list">
			<div class="live-item" id="sportgame"><img src="/mobile/img/sports_1.png" alt="" /></div>
		</div>
		<div class="slot_game_item_list">
			<div class="live-item" id="chessItem" onclick="loginQlyGame()"><img src="/mobile/img/img761.png" alt="" /></div>
		</div>
		<input id="j-baseUrl" type="hidden" value="<%=request.getRequestURL().toString().replace(request.getServletPath(), " ") %>">
		<input id="j-ptToken" type="hidden" value="">
		<input id="j-ntToken" type="hidden" value="${session.nt_session}">
		<input id="j-dtToken" type="hidden" value="${session.slotKey}">
		<input id="j-dtGameurl" type="hidden" value="${session.gameurl}">
		<input id="j-referWebsite" type="hidden" value="${session.referWebsite}">
		<input id="j-username" type="hidden" value="">
		<input id="j-isLogin" type="hidden" value="${session.customer!=null}">
		<input id="j-LoginName" type="hidden" value="${session.customer.loginname}">
		<jsp:include page="/mobile/commons/menu.jsp" />
	</body>

</html>
<script type="text/javascript" src="/mobile/js/MobileManage.js?v=1202"></script>
<script type="text/javascript" src="/mobile/js/WebApp.js?v=1202"></script>
<script src="/mobile/js/Loader.js?v=5"></script>
<script src="/mobile/app/js/jquery.lazyload-v1.9.1.min.js"></script>
<script src="/mobile/js/new/game.js?v=25"></script>
<script>
	$(function() {
		var _types = Util.getQueryString('type');
		var i = 0;
		switch(_types) {
			case "live":
				i = 1;
				break;
			case "sport":
				i = 3;
				break;
			case "fish":
				i = 2;
				break;
			case "chess":
				i = 4;
				break;
		}
		console.log(_types)
		$(".slatform-screening li").eq(i).click();
	})

	var backendPlatform = '${platform}'; //获取当前链接判断条件
	var defaultPlatform = 'PT';
	var currentPlatform = '';
	var urlplatform = Util.getQueryString('platform');
	if(backendPlatform != "") {
		currentPlatform = backendPlatform;
	} else if(urlplatform != '') {
		currentPlatform = urlplatform;
	} else {
		currentPlatform = Util.getCookie("slotgame");
	}
	if(currentPlatform == "") {
		currentPlatform = defaultPlatform;
	}
</script>
<script type="text/javascript" src="/mobile/app/js/layer/mobile/layer.js"></script>

<script type="text/javascript">
	//j-come-back 返回的判断
	var mobileorApp = window.location.href;
	var currentDevice = $('#j-ifApp');
	window.mobileManage = new MobileManage('${ctx}/', '${imgCode}');
	window.webapp = new WebApp();
	//返回
	$('#j-come-back').click(function() {
		if(mobileorApp.indexOf('openMobile') == -1) {
			history.back();
			currentDevice.val('');
		} else {
			webapp.redirect('longduwebapp://login?u=${session.customer.loginname}&p=${session.customer.password}');
			currentDevice.val('app');
		}
	})
	switch(currentPlatform) {
		case "AG":
			/*loginAGslotGame();*/
			break;
		case "SBA":
			loginSBAGame();
			break;
		case "AGFISH":
			loginAGFishGame();
			break;
		case "GGFISH":
			loginGGFishGame();
			break;
		case "AGIN":
			loginAGLiveGame();
			break;
		case "EBET":
			/*loginEBETLiveGAME();*/
			break;
		case "MWGFISH":
			loginMWGFISHGame();
			break;
		case "N2LIVE":
			n2livegame();
			break;
		case "EBET":
			/*loginEBETLiveGAME();*/
			break;
		case "TTG":
			/*loginTTGGame();*/
			break;
	}
	//沙巴体育
	$("#sportgame").on("click", function() {
		loginSBAGame();
	});
	//ag真人
	$("#aglivegame").on("click", function() {
		loginAGLiveGame();
	});
	//AG捕鱼
	$("#agfishgame").on("click", function() {
		loginAGFishGame();
	});
	$("#ebetgame").on("click", function() {
		//loginEBETGame();
	});
	$("#ebetlivegame").on("click", function() {
		loginEBETLiveGAME();
	});

	$("#mwgfishweb").on("click", function() {
		loginMWGFISHGame();
	});
	$("#n2livegame").on("click", function() {
		n2livegame();
	});
	$("#ggfishweb").on("click", function() {
		loginGGFishGame()
	});

	//n2
	function n2livegame() {
		if('${session.customer==null}' == 'true') {
			layer.open({
				content: '登录您的账号，才可进入游戏。',
				btn: '我知道了'
			});
		} else {
			layer.open({
				content: '即将跳转N2LIVE',
				skin: 'msg',
				time: 2 //2秒后自动关闭
			});
			setTimeout(
				"window.location.href='/mobi/gameNTwoRedirect.aspx'", 1000)
		}
	}
	//GG捕魚
	function loginGGFishGame() {
		if('${session.customer==null}' == 'true') {
			layer.open({
				content: '登录您的账号，才可进入游戏。',
				btn: '我知道了'
			});
		} else {
			window.location.href = '/ttLogin.aspx?gameName=EGIGame&gameType=0&gameId=14900&lang=zh-cn&deviceType=mobile';
		}
	}

	//MWG捕魚
	function loginMWGFISHGame() {
		if('${session.customer==null}' == 'true') {
			layer.open({
				content: '登录您的账号，才可进入游戏。',
				btn: '我知道了'
			});
		} else {
			$.post('/mobi/mwgLogin.aspx?gameCode=1051', function(result) {
				if(result.data != '') {
					window.location.href = result.data;
				} else {
					layer.open({
						content: result.message,
						btn: '我知道了'
					});
				}
			})
		}
	}

	//TTG老虎机
	function loginTTGGame() {
		var url = 'https://ams-games.ttms.co/casino/longfa_lh/lobby/index.html?playerHandle={0}&account={1}&lang=zh-cn&platformUrl={2}';
		window.TTplayerhandle = '${session.TTplayerhandle}';
		if(TTplayerhandle) {
			window.location.href = String.format(url, TTplayerhandle, 'CNY', window.location.href);
		} else {
			layer.open({
				type: 2
			});
			mobileManage.ajax({
				url: 'mobi/loginTTG.aspx',
				callback: function(result) {
					layer.closeAll();
					if(result.success) {
						window.TTplayerhandle = result.message;
						window.location.href = String.format(url, TTplayerhandle, 'CNY', window.location.href);
					} else {
						layer.open({
							content: result.message,
							btn: '我知道了'
						});
					}
				}
			});
		}
	}
	//GG捕魚
	function loginGGFishGame() {
		if('${session.customer==null}' == 'true') {
			layer.open({
				content: '登录您的账号，才可进入游戏。',
				btn: '我知道了'
			});
			return;
		} else {
			window.location.href = 'asp/ttLogin.aspx?gameName=EGIGame&gameType=0&gameId=14900&lang=zh-cn&deviceType=mobile';
		}
	}

	//AG捕魚
	function loginAGFishGame() {
		if('${session.customer==null}' == 'true') {
			layer.open({
				content: '登录您的账号，才可进入游戏。',
				btn: '我知道了'
			});
		}
		mobileManage.ajax({
			url: 'mobi/gameAginRedirect.aspx?agFish=1',
			callback: function(result) {
				layer.closeAll();
				if('${session.customer==null}' == 'true') {
					layer.open({
						content: '登录您的账号，才可进入游戏。',
						btn: '我知道了'
					});
				} else {
					if(result.success) {
						window.location.href = result.data.url;
					} else {
						layer.open({
							content: result.message,
							btn: '我知道了'
						});
					}
				}
			}
		});
	}
	//AG捕鱼网页版
	$('#agfishweb').click(function() {
		loginAGwebGame();
	})

	function loginAGwebGame() {
		var alertCon = '您已登录，请直接进行游戏';
		if('${session.customer==null}' == 'true') {
			layer.open({
				content: '请登入后再进入游戏！',
				btn: '我知道了'
			});
			return;
		}
		mobileManage.ajax({
			url: 'mobi/gameAginRedirect.aspx?agFish=1',
			callback: function(result) {
				layer.closeAll();
				if(result.success) {
					window.location.href = result.data.url;
				} else {
					layer.open({
						content: result.message,
						btn: '我知道了'
					});
				}
			}
		});
	}

	//AG老虎机
	function loginAGslotGame() {
		mobileManage.ajax({
			url: 'mobi/gameAginRedirect.aspx',
			callback: function(result) {
				if('${session.customer==null}' == 'true') {
					layer.open({
						content: '登录您的账号，才可进入游戏。',
						btn: '我知道了'
					});
				} else {
					if(result.success) {
						window.location.href = result.data.url;
					} else {
						layer.open({
							content: result.message,
							btn: '我知道了'
						});
					}
				}
			}
		});
	}
	//沙巴体育
	function loginSBAGame() {
		if('${session.customer==null}' == 'true') {
			layer.open({
				content: '请登入后再进入游戏！',
				btn: '我知道了'
			});
			return;
		}
		layer.open({
			content: '即将跳转沙巴体育',
			skin: 'msg',
			time: 2 //2秒后自动关闭
		});
		setTimeout("window.location.href='/asp/sbMobileLogin.aspx'", 1000)
	}
	//AG真人
	function loginAGLiveGame() {
		mobileManage.ajax({
			url: 'mobi/gameAginRedirect.aspx',
			callback: function(result) {
				layer.closeAll();
				if('${session.customer==null}' == 'true') {
					//alert('登录您的账号，才可进入游戏。');
					layer.open({
						content: '登录您的账号，才可进入游戏。',
						btn: '我知道了'
					});
				} else {
					if(result.success) {
						window.location.href = result.data.url;
					} else {
						layer.open({
							content: result.message,
							btn: '我知道了'
						});
					}
				}
			}
		});

	}

	//Ebet真人
	function loginEBETGame() {
		var downloadUrl = getMobileKind() == 'Android' ? 'https://www.ebetapp.com/applib/90/ebet.apk' : 'itms-services://?action=download-manifest&url=https://www.ebetapp.com/applib/90/ebet.plist';
		window.location.href = downloadUrl;
	}

	//EBET网页游戏
	function loginEBETLiveGAME() {
		if('${session.customer==null}' == 'true') {
			layer.open({
				content: '请登入后再进入游戏！',
				btn: '我知道了'
			});
			return;
		}
		mobileManage.ajax({
			url: 'mobi/getEbetToken.aspx',
			callback: function(result) {
				mobileManage.getLoader().close();
				layer.closeAll();
				if(result.success) {
					if(!result.data) {
						alert('进入游戏失败！');
						layer.open({
							content: '进入游戏失败',
							btn: '我知道了'
						});
						return;
					}
					window.location.href = String.format(url, result.data.loginname, result.data.accessToken)
				} else {
					layer.open({
						content: result.message,
						btn: '我知道了'
					});
				}
			}
		});

	}
</script>
<script src="/js/countUpNew.js?v=2"></script>
<script>
	/*DT奖池*/
	$.post("/asp/dtJackpot.aspx", function(response) {
		console.log(response.pot);
		if(CountUp) {
			var demo = new CountUp("j-jackpotCount", response.pot || 40194572, 9194572, 2, 3000000000, {
				useEasing: true,
				useGrouping: true,
				separator: ',',
				decimal: '.',
				formate: true,
				suffix: ''
			});
			demo.start();
		}
	});
</script>
<script>
	$('#filter-2 li').on('click',function () {
		var $this = $(this),tabAttr = $(this).find('a').attr('data-tab');
		$this.addClass('active').siblings('.active').removeClass('active');
		$('#filterUl').find('a[data-tab="'+tabAttr+'"]').trigger('click');
	})
</script>
<script>
	//奇乐游
	function loginQlyGame() {
		if('${session.customer==null}' == 'true') {
			layer.open({
				content: '请登入后再进入游戏！',
				btn: '我知道了'
			});
			return;
		}
		layer.open({
			content: '即将跳转棋乐游棋牌',
			skin: 'msg',
			time: 2 //2秒后自动关闭
		});
		setTimeout("window.location.href='/game/cg761Login.aspx'", 1000)
	}
</script>
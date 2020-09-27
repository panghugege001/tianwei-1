// 自助优惠配置->优惠平台->优惠类型
var preferential_platform = {
	"6001": { 'value': '6001', 'text': 'PT存送优惠', "type": [ { 'value': '590', 'text': '自助PT首存优惠' }, { 'value': '591', 'text': '自助PT次存优惠' }, { 'value': '705', 'text': '自助PT限时优惠' } ] },
	"6010": { 'value': '6010', 'text': 'SW存送优惠', "type": [ { 'value': '794', 'text': '自助SW首存优惠' }, { 'value': '795', 'text': '自助SW次存优惠' }, { 'value': '796', 'text': '自助SW限时优惠' } ] },
	"6002": { 'value': '6002', 'text': 'MG存送优惠', "type": [ { 'value': '730', 'text': '自助MG首存优惠' }, { 'value': '731', 'text': '自助MG次存优惠' }, { 'value': '732', 'text': '自助MG限时优惠' } ] },
	"6003": { 'value': '6003', 'text': 'DT存送优惠', "type": [ { 'value': '733', 'text': '自助DT首存优惠' }, { 'value': '734', 'text': '自助DT次存优惠' }, { 'value': '735', 'text': '自助DT限时优惠' } ] },
	"6004": { 'value': '6004', 'text': 'QT存送优惠', "type": [ { 'value': '710', 'text': '自助QT首存优惠' }, { 'value': '711', 'text': '自助QT次存优惠' }, { 'value': '712', 'text': '自助QT限时优惠' } ] },
	"6006": { 'value': '6006', 'text': 'TTG存送优惠', "type": [ { 'value': '598', 'text': '自助TTG首存优惠' }, { 'value': '599', 'text': '自助TTG次存优惠' }, { 'value': '706', 'text': '自助TTG限时优惠' } ] },
	"6007": { 'value': '6007', 'text': 'PNG存送优惠', "type": [ { 'value': '740', 'text': '自助PNG首存优惠' }, { 'value': '741', 'text': '自助PNG次存优惠' }, { 'value': '742', 'text': '自助PNG限时优惠' } ] },
	"6008": { 'value': '6008', 'text': 'AG真人存送优惠', "type": [ { 'value': '743', 'text': '自助AG真人首存优惠' }, { 'value': '744', 'text': '自助AG真人次存优惠' }, { 'value': '745', 'text': '自助AG真人限时优惠' } ] }
	/*
	"6005": { 'value': '6005', 'text': 'NT存送优惠', "type": [ { 'value': '707', 'text': '自助NT首存优惠' }, { 'value': '708', 'text': '自助NT次存优惠' }, { 'value': '709', 'text': '自助NT限时优惠' } ] },
	"6009": { 'value': '6009', 'text': '老虎机存送优惠', "type": [ { 'value': '791', 'text': '自助老虎机首存优惠' }, { 'value': '792', 'text': '自助老虎机次存优惠' }, { 'value': '793', 'text': '自助老虎机限时优惠' } ] }*/
};

// 自助优惠配置->次数类别
var preferential_times_type = [ 
	{ 'value': 1, 'text': '日' }, 
	{ 'value': 2, 'text': '周' }, 
	{ 'value': 3, 'text': '月' }, 
	{ 'value': 4, 'text': '年' } 
];
var preferential_entrance = [
    { 'value': 'pc', 'text': '官网' },
    { 'value': 'android', 'text': '安卓端' },
    { 'value': 'apple', 'text': '苹果端' },
    { 'value': 'html5', 'text': 'html5' }
];
// 红包雨活动配置->转入平台

var turn_platform = [
    { 'value': 'SELF', 'text': '主账户' },
    { 'value': 'PT', 'text': 'PT' },
    { 'value': 'MG', 'text': 'MG' },
    { 'value': 'DT', 'text': 'DT' },
    { 'value': 'QT', 'text': 'QT' },
    { 'value': 'NT', 'text': 'NT' },
    { 'value': 'TTG', 'text': 'TTG' },
    { 'value': 'PNG', 'text': 'PNG' }
];

//自助优惠配置->等级
var preferential_grade = [
	{ 'value': '0', 'text': '天兵' },
	{ 'value': '1', 'text': '天将' },
	{ 'value': '2', 'text': '天王' },
	{ 'value': '3', 'text': '星君' },
	{ 'value': '4', 'text': '真君' },
	{ 'value': '5', 'text': '仙君' },
	{ 'value': '6', 'text': '帝君' },
	{ 'value': '7', 'text': '天尊' },
	{ 'value': '8', 'text': '天帝' }
];

// 自助优惠配置->申请通道
var preferential_passage = [
	{ 'value': '1', 'text': '官网' },
	{ 'value': '2', 'text': 'WEB' },
	{ 'value': '3', 'text': '安卓APP' },
	{ 'value': '4', 'text': '苹果APP' }
];


//自助体验金配置->APP下载彩金平台
var app_type = [
	{ 'value': 'PT', 'text': 'PT' },
	{ 'value': 'MG', 'text': 'MG' },
	{ 'value': 'DT', 'text': 'DT' },
	{ 'value': 'QT', 'text': 'QT' },
	{ 'value': 'NT', 'text': 'NT' },
	{ 'value': 'TTG', 'text': 'TTG' },
	{ 'value': 'PNG', 'text': 'PNG' },
	{ 'value': 'SLOT', 'text': 'SLOT' }
];

//自助体验金配置->体验金平台
var tyj_type = [
	{ 'value': 'PT', 'text': 'PT' },
	{ 'value': 'SW', 'text': 'SW' },
	{ 'value': 'MG', 'text': 'MG' },
	{ 'value': 'DT', 'text': 'DT' },
	{ 'value': 'QT', 'text': 'QT' },
	{ 'value': 'TTG', 'text': 'TTG' },
	{ 'value': 'PNG', 'text': 'PNG' },
	{ 'value': 'NT', 'text': 'NT' }
];


//优惠券配置->游戏平台
var coupon_platform = [
	{ 'value': 'PT', 'text': 'PT' },
	{ 'value': 'SW', 'text': 'SW' },
	{ 'value': 'MG', 'text': 'MG' },
	{ 'value': 'DT', 'text': 'DT' },
	{ 'value': 'QT', 'text': 'QT' },
	{ 'value': 'TTG', 'text': 'TTG' },
	{ 'value': 'PNG', 'text': 'PNG' },
	{ 'value': 'AGIN', 'text': 'AGIN' }
];

//优惠券配置->优惠券类型
var coupon_type = [
	{ 'value': '319', 'text': '存送优惠券' },
	{ 'value': '419', 'text': '红包优惠券' }
];

// 红包雨活动配置->转入平台


var turn_platform = [
    { 'value': 'SELF', 'text': '主账户' },
    { 'value': 'PT', 'text': 'PT' },
    { 'value': 'MG', 'text': 'MG' },
    { 'value': 'DT', 'text': 'DT' },
    { 'value': 'QT', 'text': 'QT' },
    { 'value': 'NT', 'text': 'NT' },
    { 'value': 'TTG', 'text': 'TTG' },
    { 'value': 'PNG', 'text': 'PNG' }
];
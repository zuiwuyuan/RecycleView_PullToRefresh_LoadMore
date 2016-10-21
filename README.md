# RecycleView_PullToRefresh_LoadMore
整合多个开源项目，自认为相当简便使用的RecyclerView，支持下拉刷新，加载更多，添加Header和Footer。

##功能：
 1. 普通列表功能、为列表添加分割线，设置分割线高度（宽度）、颜色等。
 2. 网格列表功能、为网格列表添加分割线，设置分割线高度（宽度）、颜色等。
 3. 普通列表、网格列表均支持滑动加载更多数据功能，并在网络失败情况下，添加了提醒重新加载功能（可看下面示意图）。
 4. 普通列表、网格列表均支持添加Header功能。 再不需要加载更多数据情况下，支持添加Footer。
 5. 未扩展下拉刷新功能，如需要，请使用Android官方提供的SwipeRefreshLayout 。

 

##Thanks：
 在介绍我整合的项目之前，有必要先说明下我整合的两个最重要的库，他们才是真的原创，希望大家尊重原创。该项目主要整合了两个开源项目，是在它们的基础上进行的再创造：
 1. RecyclerView分割线：https://github.com/yqritc/RecyclerView-FlexibleDivider
 2. RecyclerView添加HeaderView和FooterView：https://github.com/cundong/HeaderAndFooterRecyclerView


##效果图：

· 垂直列表，添加分割线

![image](https://github.com/zuiwuyuan/RecycleView_PullToRefresh_LoadMore/blob/master/imgs/2.png)

· 网格列表，添加分割线

![image](https://github.com/zuiwuyuan/RecycleView_PullToRefresh_LoadMore/blob/master/imgs/3.png)

· 垂直列表，添加HeaderView

![image](https://github.com/zuiwuyuan/RecycleView_PullToRefresh_LoadMore/blob/master/imgs/4.png)

· 网格列表，添加HeaderView

![image](https://github.com/zuiwuyuan/RecycleView_PullToRefresh_LoadMore/blob/master/imgs/5.png)


· 加载更多数据网络错误时，提示重新加载。

![image](https://github.com/zuiwuyuan/RecycleView_PullToRefresh_LoadMore/blob/master/imgs/6.png)

## 实现：
  关于该库是如何整合的，此处不多介绍的，有兴趣的人可以下载源码去看看，当然，不足之处，也可以进行再次扩展。
 关于该库的使用，我还是建议大家该library下载下来，导入自己项目，这样也方便自己随意修改使用。

 **友情提醒： 请一定使用HeaderAndFooterRecyclerViewAdapter对你的ViewAdapter进行再次封装，因为在项目的整改过程中，很多地方，我使用了该类对列表是否有Header或者Footer进行了判断，所以如果你不用HeaderAndFooterRecyclerViewAdapter，则可能会出现问题。**
 
###1. 实现垂直列表
 ``` 
private void initView() {

        listWeChats = (RecyclerView) findViewById(R.id.listWeChats);

        mDatas = new ArrayList<>();

        WeChatListAdapter weChatListAdapter = new WeChatListAdapter(this, mDatas, onClickListener);
        recyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(weChatListAdapter);
        listWeChats.setAdapter(recyclerViewAdapter);

        listWeChats.setLayoutManager(new LinearLayoutManager(this));

        listWeChats.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(WChatListActivity.this)
                        .color(Color.parseColor("#00c7c0"))
                        .sizeResId(R.dimen.list_divider_height)
                        .showLastDivider()
                        .marginResId(R.dimen.list_divider_left_margin, R.dimen.list_divider_right_margin)
                        .build());

        listWeChats.addOnScrollListener(mOnScrollListener);
    }
 ``` 
 
 简析：HeaderAndFooterRecyclerViewAdapter主要是为了是对RecyclerView.Adapter的扩展实现，通过它，可以添加HeaderView和FooterView（具体可看源码）。HorizontalDividerItemDecoration则是扩展了RecyclerView.ItemDecoration，用于为垂直列表添加分割线，如果你是一个横向的列表，则可以使用另一个ItemDecoration的实现：VerticalDividerItemDecoration。具体的使用可参加项目的示例。
 

###2. 实现网格列表
``` 
 private void initView() {

        listWeChats = (RecyclerView) findViewById(R.id.listWeChats);

        mDatas = new ArrayList<>();

        WeChatListAdapter weChatListAdapter = new WeChatListAdapter(this, mDatas, onClickListener);

        // 必须将Adapter再次封装
        HeaderAndFooterRecyclerViewAdapter recyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(weChatListAdapter);
        listWeChats.setAdapter(recyclerViewAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup(listWeChats.getAdapter(), gridLayoutManager.getSpanCount()));
        listWeChats.setLayoutManager(gridLayoutManager);

        Drawable mDivider = getResources().getDrawable(R.drawable.list_divider);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00c7c0"));

        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration.Builder(this, gridLayoutManager.getSpanCount())
                .setH_spacing(50)
                .setV_spacing(50)
//                .setmDivider(mDivider)
//                .setmDivider(colorDrawable)
                .setDividerColor(Color.parseColor("#00c7c0"))
                .build();

        listWeChats.addItemDecoration(itemDecoration);

        listWeChats.addOnScrollListener(mOnScrollListener);
    }
 ``` 
简析：GridSpacingItemDecoration 用于为列表添加分割线。具体的使用可参加项目的示例。

 
###3. 实现带有HeaderView的垂直列表
``` 
private void initView() {

        listWeChats = (RecyclerView) findViewById(R.id.listWeChats);

        mDatas = new ArrayList<>();


        WeChatListAdapter weChatListAdapter = new WeChatListAdapter(this, mDatas, onClickListener);
        HeaderAndFooterRecyclerViewAdapter  recyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(weChatListAdapter);

        listWeChats.setAdapter(recyclerViewAdapter);

        listWeChats.setLayoutManager(new LinearLayoutManager(this));

        listWeChats.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(WChatListHeaderAndFooterActivity.this)
                        .colorResId(R.color.list_divider_color)
//                        .color(Color.parseColor("#FF0000"))
                        .sizeResId(R.dimen.list_divider_height)
                        .marginResId(R.dimen.list_divider_left_margin, R.dimen.list_divider_right_margin)
                        .hasHeader()
                        .build());

        listWeChats.addOnScrollListener(mOnScrollListener);

        // 如果你要添加HeaderView，则必须使用HeaderAndFooterRecyclerViewAdapter
        RecyclerViewUtils.setHeaderView(listWeChats, new SampleHeader(this));
    }
 ``` 
简析：RecyclerViewUtils，可以很方便的为RecyclerView添加HeaderView。

 
###4. 实现带有HeaderView的网格布局
```  
private void initView() {

        listWeChats = (RecyclerView) findViewById(R.id.listWeChats);

        mDatas = new ArrayList<>();

        WeChatListAdapter weChatListAdapter = new WeChatListAdapter(this, mDatas, onClickListener);

        HeaderAndFooterRecyclerViewAdapter  recyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(weChatListAdapter);
        listWeChats.setAdapter(recyclerViewAdapter);

         GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
         gridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup(listWeChats.getAdapter(), gridLayoutManager.getSpanCount()));
         listWeChats.setLayoutManager(gridLayoutManager);

        Drawable mDivider = getResources().getDrawable(R.drawable.list_divider);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F0C7C0"));

        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration.Builder(this, gridLayoutManager.getSpanCount())
                .hasHeader()
                .setH_spacing(50)
                .setV_spacing(50)
//                .setDividerColor(Color.parseColor("#008E00"))
                .setmDivider(colorDrawable)
//                .setmDivider(mDivider)
                .build();

        listWeChats.addItemDecoration(itemDecoration);

        listWeChats.addOnScrollListener(mOnScrollListener);

        RecyclerViewUtils.setHeaderView(listWeChats, new SampleHeader(this));

    }
``` 


##拓展：
 
 
 ![image](https://github.com/zuiwuyuan/RecycleView_PullToRefresh_LoadMore/blob/master/imgs/loader_footer.png)

**如上图、我将加载更多、网络错误的footerview整合在了library库中，所以如果您需要对底部加载更多等页面进行自定义的话，请下载library库直接修改、并导入自己的项目中直接使用**
 

***如果对你有帮助，请star下吧，谢谢。***



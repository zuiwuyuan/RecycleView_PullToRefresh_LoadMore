# RecycleView_PullToRefresh_LoadMore
整合多个开源项目，自认为相当简便使用的RecyclerView，支持下拉刷新，加载更多，添加Header和Footer。

功能：
 1. 普通列表功能、为列表添加分割线，设置分割线高度（宽度）、颜色等。
 2. 网格列表功能、为网格列表添加分割线，设置分割线高度（宽度）、颜色等。
 3. 普通列表、网格列表均支持滑动加载更多数据功能，并在网络失败情况下，添加了提醒重新加载功能（可看下面示意图）。
 4. 普通列表、网格列表均支持添加Header功能。 再不需要加载更多数据情况下，支持添加Footer。
 5. 未扩展下拉刷新功能，如需要，请使用Android官方提供的SwipeRefreshLayout 。

 在介绍我整合的项目之前，有必要先说明下我整合的两个最重要的库，他们才是真的原创，希望大家尊重原创。
Thanks：
 该项目主要整合了两个开源项目，是在它们的基础上进行的再创造：
 1. RecyclerView分割线：https://github.com/yqritc/RecyclerView-FlexibleDivider
 2. RecyclerView添加HeaderView和FooterView：https://github.com/cundong/HeaderAndFooterRecyclerView

效果图：
· 垂直列表，添加分割线


· 网格列表，添加分割线


· 垂直列表，添加HeaderView


· 网格列表，添加HeaderView



· 加载更多数据网络错误时，提示重新加载。


 实现：
  关于该库是如何整合的，此处不多介绍的，有兴趣的人可以下载源码去看看，当然，不足之处，也可以进行再次扩展。
 关于该库的使用，我还是建议大家该library下载下来，导入自己项目，这样也方便自己随意修改使用。

 1. 实现垂直列表
[java] view plain copy print?在CODE上查看代码片派生到我的代码片
private void initView() {

        listWeChats = (RecyclerView) findViewById(R.id.listWeChats);

        mDatas = new ArrayList<>();

        WeChatListAdapter weChatListAdapter = new WeChatListAdapter(this, mDatas, onClickListener);
        HeaderAndFooterRecyclerViewAdapter  recyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(weChatListAdapter);
        listWeChats.setAdapter(recyclerViewAdapter);

        listWeChats.setLayoutManager(new LinearLayoutManager(this));
        listWeChats.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(WChatListActivity.this)
                        .color(Color.parseColor("#00c7c0"))
                        .sizeResId(R.dimen.list_divider_height)
                        .showLastDivider()
                        .marginResId(R.dimen.list_divider_left_margin, R.dimen.list_divider_right_margin)
                        .build(false));

        listWeChats.addOnScrollListener(mOnScrollListener);
    }
 简析：HeaderAndFooterRecyclerViewAdapter主要是为了是对RecyclerView.Adapter的扩展实现，通过它，可以添加HeaderView和FooterView（具体可看源码）。HorizontalDividerItemDecoration则是扩展了RecyclerView.ItemDecoration，用于为垂直列表添加分割线，如果你是一个横向的列表，则可以使用另一个
ItemDecoration的实现：VerticalDividerItemDecoration。具体的使用可参加项目的示例。

2. 实现网格列表
[java] view plain copy print?在CODE上查看代码片派生到我的代码片
 private void initView() {

        listWeChats = (RecyclerView) findViewById(R.id.listWeChats);

        mDatas = new ArrayList<>();

        WeChatListAdapter weChatListAdapter = new WeChatListAdapter(this, mDatas, onClickListener);

        HeaderAndFooterRecyclerViewAdapter recyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(weChatListAdapter);
        listWeChats.setAdapter(recyclerViewAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) listWeChats.getAdapter(), gridLayoutManager.getSpanCount()));
        listWeChats.setLayoutManager(gridLayoutManager);

//        Drawable mDivider = getResources().getDrawable(R.drawable.list_divider);
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00c7c0"));

        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(this, 2);
        itemDecoration.setH_spacing(50);
        itemDecoration.setV_spacing(50);
//        itemDecoration.setmDivider(mDivider);

        itemDecoration.setDividerColor(Color.parseColor("#008E00"));

        listWeChats.addItemDecoration(itemDecoration);

        listWeChats.addOnScrollListener(mOnScrollListener);
    }
简析：GridSpacingItemDecoration 用于为列表添加分割线。具体的使用可参加项目的示例。

3. 实现带有HeaderView的垂直列表
[java] view plain copy print?在CODE上查看代码片派生到我的代码片
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
                        .build(true));

        listWeChats.addOnScrollListener(mOnScrollListener);

        RecyclerViewUtils.setHeaderView(listWeChats, new SampleHeader(this));
    }
简析：RecyclerViewUtils，可以很方便的为RecyclerView添加HeaderView。

4. 实现带有HeaderView的网格布局
[java] view plain copy print?在CODE上查看代码片派生到我的代码片
 private void initView() {

        listWeChats = (RecyclerView) findViewById(R.id.listWeChats);

        mDatas = new ArrayList<>();

        WeChatListAdapter weChatListAdapter = new WeChatListAdapter(this, mDatas, onClickListener);

        HeaderAndFooterRecyclerViewAdapter recyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(weChatListAdapter);
        listWeChats.setAdapter(recyclerViewAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) listWeChats.getAdapter(), gridLayoutManager.getSpanCount()));
        listWeChats.setLayoutManager(gridLayoutManager);

//        Drawable mDivider = getResources().getDrawable(R.drawable.list_divider);
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00c7c0"));

        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(this, 2);
        itemDecoration.setHasHeader(true);
        itemDecoration.setH_spacing(50);
        itemDecoration.setV_spacing(50);
//        itemDecoration.setmDivider(mDivider);

        itemDecoration.setDividerColor(Color.parseColor("#008E00"));

        listWeChats.addItemDecoration(itemDecoration);

        listWeChats.addOnScrollListener(mOnScrollListener);

        RecyclerViewUtils.setHeaderView(listWeChats, new SampleHeader(this));
    }

  以上就是我整合使用的RecyclerView，挺顺手的，我已经在多个项目中使用它了，希望更多的人能喜欢支持它。



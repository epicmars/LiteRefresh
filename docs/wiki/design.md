# LiteRefresh设计文档

## **简介**

一个基于CoordinatorLayout实现的**嵌套滑动**库，支持**下拉刷新**与**上拉加载**，通过对市面上常见APP的UI交互进行分析，可以发现，由于目前智能手机都是长方形触控屏幕的设计，APP中以滑动列表的形式提供UI交互的场景占绝大多数，尤其是下拉刷新与上拉加载这两种交互方式，在网络加载的场景下几乎随处可见，例如一些新闻资讯类应用基本都属于这类：

gif 微博 gif 网易新闻 gif 今日头条

在上面这种经典的下拉刷新与上拉加载交互中，头部和尾部视图都是跟随列表一起移动。但这两种交互的一些变体也十分常见，例如屏幕上方的一个头部在下拉刷新时跟随列表一起向下滑动，但当列表向上滑动时，头部保持在顶部位置不动或者收缩变小以留出更多空间用于展示列表。

gif 美团 gif 支付宝首页 gif 哔哩哔哩首页推荐

还有类似淘宝2楼的效果在商场类APP也比较常见，当内容列表下拉到一定程度时，释放会进行内容刷新，当继续下拉时会释放会展示一个新的页面：

gif 淘宝首页 gif 京东首页

另一类比较典型的交互场景是导航地图和打车类应用的卷帘门交互效果，类似谷歌Material UI交互设计中的backdrop和bottom sheet，其中地图作为基本内容显示在最底层，上层是各种控件和内容的展示，其中内容列表可以逐级从底部展开，一般位于底部，当下拉到最底部时仅显示少部分UI控件，当上拉一部分，可以展示更多的操作界面，再进一步向上滑动会展示更多信息，这时向下拉列表可以分段将列表锚定在不同的地方。

gif 百度首页  gif 高德首页

通过分析这些交互效果，我们可以总结出一个通用的交互逻辑，“下拉刷新”与“上拉加载更多”只是这种交互逻辑可以实现的众多交互效果中的一小部分，它可以支持容易地实现更多不同功能的嵌套滑动交互效果。

## **设计原则**

简单易用

- 支持纵向和横向滑动
- 支持下拉刷新与上拉加载
- 支持简介中的全部使用场景（写在Demo中，完全还原）

## **功能**

CoordinatorLayout的直接子view的依赖关系由一个有向无环图进行维护，即一个单向依赖关系，这也是我们期望的，因为循环依赖会使得设计的复杂度成倍增加，易于引发意料之外的错误，并且不能带来功能上的增强。

在LiteRefresh 0.10.3版本的设计中，由于最初的设计是围绕解决**下拉刷新**与**上拉加载**这两种使用场景设计的，因此将子View类型分为content、header、footer三种类型，由于header和footer都可以认为是content的指示器，因此header和footer都可以合并为indicator一类，最终可以将子View类型分为content和indicator两类，其中content指实现了接口ViewParent所定义的嵌套滑动接口的相关ViewGroup子类，主要包括ListView、RecyclerView、NestedScrollView，这几个View的特点都是一旦它们的内部View的范围超过了其自身的显示范围，支持通过滑动操作来显示出隐藏部分，也即它们都是可滑动的，header和footer可以是其它类型的任意控件，一般是整个视图中的一块。

对于content子View的列表不一定是整个视图和核心内容，也有可能是补充性、临时性内容，即其用途是多样的，根据这样的抽象层级，我们可以将子View划分为两种类型，即可滑动View和块View，这两种View对应的行为定义为可滑动行为和块行为。

### 偏移量

偏移量的大小根据View坐标系来确定，CoordinatorLayout的直接子View的偏移量坐标原点为CoordinatorLayout的左上角，向右为x轴正向，向下为y轴正向。

jpg View坐标系

### 可滑动行为

可以有1到多个

手指向下滑动，当Scrollable View内部向下滑动直到其顶部可见并且无法再滑动时，如果手指继续下滑操作，可滑动Behavior决定如何消费掉向下的滑动操作，目前提供的的行为都是改变可滑动View的偏移量：

1. 根据手势滑动操作的方向让可滑动View整体进行偏移
2. 可以设定偏移量锚点，初始位置是偏移量锚点的一种
3. 可以设置偏
4. 移停止点

同理，对于手指向上，向左，向右的可滑动View的交互逻辑是一样的，只是方向不同而已，这里仅以向下滑动进行设计。

#### 锚点

可以设置1个或多个锚点

如果没有明确设置锚点，view的初始位置会作为默认的锚点。当可滑动View偏移到指定锚点位置附近时，可以锚定在该位置，即当手势操作结束时view可自动恢复到距离最近的锚点位置，也可控制view恢复到其中任一锚点位置。

**判定：**

- View位置偏移量 == 锚点
- View位置偏移量  <  锚点
  - abs(View位置偏移量 - 锚点) ≤ abs(View位置偏移量 - 上一锚点) ，释放停到当前锚点
  - abs(View位置偏移量 - 锚点) > abs(View位置偏移量 - 上一锚点) ，释放停到上一锚点
- View位置偏移量  >  锚点
  - abs(View位置偏移量 - 锚点) ≤ abs(View位置偏移量 - 下一锚点) ，释放停到当前锚点
  - abs(View位置偏移量 - 锚点) ≤ abs(View位置偏移量 - 下一锚点) ，释放停到下一锚点

即释放后停到**距离最近的锚点**。

#### 触发点

可以有0个或多个触发点

当可滑动View偏移到指定触发点时，释放触控操作会产生触发事件。

**判定：**

- 对于View上边，View位置偏移量 ≥ 触发点时，释放手指可触发
- 对于View下边，View位置偏移量 ≤ 触发点时，释放手指可触发

#### 停止点

对于View的一个边，可以有0个到2个停止点。

停止点是View位置偏移的边界，对于View的一个边，存在一个偏移量区间，由最小和最大偏移量定义向，它们限定了一个边的偏移范围，即偏移不能超过停止点。例如根据我们设定的坐标轴，对于View上边界，最小偏移量是向上可以偏移到的位置，最大偏移量是向下可以偏移到的位置。

**判定：**最小偏移量 ≤ View位置偏移量 ≤ 最大偏移量

**停止点冲突：**由于View的高度可能固定，当一个边的偏移量满足停止点限制时，另一个边的偏移量可能不再满足停止点限制，这时以优先级更高的停止点为准。

#### 触控手势

- 拖动手势（drag）
  - 从手指接触屏幕开始，到滑动View，最后手指离开屏幕，对View而言有两种状态，即**静止-滑动-静止**
  - 实际需要使用到的是View的状态结合手势一起的状态，即**静止**-**手指按下**-**滑动**-**手指离开-静止**，注意这里的滑动指View自身位置偏移量的变化，不是其内部的滚动
- 投掷手势（fling ）暂不考虑

#### 状态机

### 块行为

根据上述对CoordinatorLayout直接子View的划分，块行为对应于块View，是相对于可滑动行为的另一类行为，它一般不直接发起嵌套滑动事件，而是依赖于可滑动行为。根据这种依赖关系，块行为所约束的块View可以保持静止、可以跟随可滑动View一起移动、可以根据可滑动View的位置偏移来改变自身长或高，也可以根据可滑动View位置偏进度来其它进行任意的运动，可以说千变万化，就看UI交互如何发挥想象了。因此无法定义出一个完全通用的块行为，这里只是根据已有的常用交互规则总结出一个可复用的框架，并定义一些经典的块行为，比如经典的带进度指示的下拉刷新头部和上拉加载尾部，希望使用该库的用户更多的在这个框架的基础上创建**自定义块行为，以满足自身复杂多变的交互需求。**

在这个框架中，块行为建立在可滑动行为的基础上，需要满足如下几点：

- 依赖于可滑动Behavior
- 知道可滑动View当前的位置和状态

#### 指示器

0到多个

依赖于可滑动行为，但可以有四种不同的模式供选择，即**静止，跟随，向上跟随，向下跟随**

头部

指示器的特殊情况，位置一般位于内容的上方

尾部

与头部功能和行为一致，移动方向相反，位置一般位于内容的下方

刷新

## 实现

```java
CoordinatorLayout.Behavior的核心接口方法

// CoordinatorLayout的子View尝试发起一个嵌套滑动，返回true表示Behavior希望接受该嵌套滑动
// child: Behavior关联的子View
// direcTargetChild: CoordinatorLayout的直接子View，它可能是target或者包含target
// target: 发起嵌套滑动的view，它是CoordinatorLayout的子View，但不一定是直接子View
public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                @NonNull V child, @NonNull View directTargetChild, @NonNull View target,
                @ScrollAxis int axes, @NestedScrollType int type)

// 一个嵌套滑动结束了
public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                @NonNull V child, @NonNull View target, @NestedScrollType int type)

// 当一个进行中的**嵌套滑动已经更新并且target已经滑动或尝试滑动**，每当嵌套滑动被嵌套滑动子view
// 更新时会调用该方法
// target: 进行嵌套滑动的CoordinatorLayout直接子View
// dxConsumed: target自身滑动操作消费的水平像素
// dyConsumed: target自身滑动操作消费的垂直像素
// dxUnconsumed: 用户请求的，但未被target自身滑动操作消费的水平像素
// dyUnconsumed: 用户请求的，但未被target自身滑动操作消费的垂直像素
// consumed: 输出，该方法返回时应该包含被该Behavior消费的距离
public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                int dyUnconsumed, @NestedScrollType int type, @NonNull int[] consumed)

// 当一个进行中的**嵌套滑动即将要更新并且在target消费掉任意滑动距离之前调用**，每次嵌套滑动子view更新
// 嵌套滑动并且在自己消费掉嵌套滑动距离前会调用该方法
// dx - 用户尝试滑动的原始水平像素数
// dy - 用户尝试滑动的原始垂直像素数，负值表示向下滑动，正值表示向上滑动
// consumed - 输出，consumed[0]设置为dx被消费的距离，consumed[1]被设置为dy被消费的距离
public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout,
                @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed,
                @NestedScrollType int type)

// 当一个嵌套滑动子View正在开始一个投掷操作。
// velocityX - 要开始的投掷的水平速度
// velocityY - 要开始的投掷的垂直速度
// consumed - 嵌套滑动子View是否消费了这个fling
// 返回Behaivor是否消费这个fling
public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout,
                @NonNull V child, @NonNull View target, float velocityX, float velocityY,
                boolean consumed)

// 当一个嵌套滑动子View即将开始一个投掷操作。
// 返回Behaivor是否消费这个fling
public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout,
                @NonNull V child, @NonNull View target, float velocityX, float velocityY)
```

```java
View.java

/**
 * 沿给定轴发起一个嵌套滑动操作，发起嵌套滑动的View遵循如下规则：
 * 在准备发起一个滑动操作时会调用startNestedScroll，在一个触控滑动中这相当于
 * 初始MotionEvent.ACTION_DOWN。在触控滑动的情况下嵌套滑动会
 * 以ViewParent.requestDisallowInterceptTouchEvent(boolean)同样的方式被自动终止。
 * 在程序化的滑动中，调用者必须显式地调用stopNestedScroll()来表明嵌套滑动结束。
 * 
 * 如果startNestedScroll返回true，那就是存在一个支持嵌套滑动的父布局。如果返回false，
 * 那么调用者可以忽略协议中的剩余部分，直到发起下个滑动。当一个嵌套滑动正在进行时调用
 * startNestedScroll会返回true。
 * 
 * 在滑动的每个增量步长中，调用者一旦计算出了请求滑动的delta就应当调用dispatchNestedPreScroll，
 * 如果它返回true，表面嵌套滑动父布局消费了部分或全部的滑动，这时调用者就应该相应调整
 * 自身滑动的距离。
 * 
 * 在应用了剩余的滑动delta后，调用者应当调用dispatchNestedScroll来将消费的delta和未消费的delta
 * 进行传递，一个嵌套滑动父布局会使用不同的方式来对待这些值，见ViewParent.onNestedScroll(View, int, int, int, int)
 */
public boolean startNestedScroll(int axes)

// 停止正在进行的嵌套滑动，如果一个嵌套滑动没有正在进行，调用该方法没有副作用。
public void stopNestedScroll()

/**
 * 分发一个进行中的嵌套滑动步长。
 * 
 * 支持嵌套滑动的View实现应该调用该方法来将正在进行中的嵌套滑动信息报告给其嵌套滑动父布局，
 * 如果一个嵌套滑动不是正在进行或者该view的嵌套滑动没有enabled，那么该方法什么也不做。 
 * 
 * 一个具有兼容性的View实现还应该在它们自身消费掉滑动事件的一部分之前
 * 调用dispatchNestedPreScroll方法。
 */
public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
            int dxUnconsumed, int dyUnconsumed, @Nullable @Size(2) int[] offsetInWindow)

/**
 * 在View自身消费掉一个正在进行的嵌套滑动步长的任意一部分之前对其进行分发。
 * 
 * 嵌套滑动前事件对于嵌套滑动事件，就像触控截取之于触控。在一个嵌套滑动中
 * dispatchNestedPreScroll为父view提供了这样的机会，即在子view消费滑动操作之前消费
 * 一些或全部的这些滑动操作。
 */
public boolean dispatchNestedPreScroll(int dx, int dy,
            @Nullable @Size(2) int[] consumed, @Nullable @Size(2) int[] offsetInWindow)
```

## 总结

1. 基于CoordinatorLayout实现，除非该类被废弃，
2. 对于类似RecyclerView这样的列表，在不改变列表对应Adapter的情况下，给列表添加一个头部布局或尾部布局，可以跟随列表一起滑动

实例

开发为一个小APP集合
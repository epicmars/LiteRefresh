LiteRefresh
=============
[ ![Download](https://api.bintray.com/packages/jastrelax/maven/com.androidpi%3Aliterefresh/images/download.svg) ](https://bintray.com/jastrelax/maven/com.androidpi%3Aliterefresh/_latestVersion)

When the right behaviors are attached to the right and direct child views of CoordinatorLayout
and when the content view to which the content behavior is attached is scrolling, 
the header and footer views can react to that scrolling, the content behavior also has internal 
state machines that trace it's scrolling states, so the pull-to-refresh and pull-to-load features are 
well supported.

## Behaviors
There are three type of behaviors can be used to attached to direct children of CoordinatorLayout.

- `RefreshContentBehavior`
    + Make nested scrolling content partially visible
    + Add scrolling listeners to observe content view's scrolling offset 
    + Add refreshing state listener to observe the refreshing state
    + Add loading state listener to observe the loading state
    
- `RefreshHeaderBehavior`
    + Make header view partially visible or totally visible
    + Make header view follow with content view or not
    + Add scrolling listeners to observe header view's scrolling offset
    + Add refreshing state listener to observe the refreshing state
    
- `RefreshFooterBehavior`
    + Make footer view partially visible or totally visible
    + Make footer view follow with content view or not
    + Add scrolling listeners to observe footer view's scrolling offset
    + Add loading state listener to observe the loading state

## Mode of header and footer behaviors

Follow | Follow Up | Follow Down | Still
----|----|-----|-----
![img](/docs/images/follow.gif) | ![img](/docs/images/follow_up.gif) | ![img](/docs/images/follow_down.gif) | ![img](/docs/images/still.gif)

When the content view is scrolling, the header and footer can decide whether to follow or not. 
Four modes are defined:

- Follow: follow with content
- Follow up: follow with content when scrolling up, but not down
- Follow down: follow with content when scrolling down, but not up
- Still: stay still
    
## Samples 

Showcases from sample application can be visited in the [showcases page](/docs/showcases.md).
    
## QuickStart
### Attach behaviors in layout
#### Use content behavior standalone
Just make your nested scrollable view a direct child of `CoordinatorLayout`, and set
the `layout_behavior` attribute to `@string/lr_refresh_content_behavior`.

```xml
    <androidx.coordinatorlayout.widget.CoordinatorLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/coordinator_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recycler_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/lr_refresh_content_behavior">
        </androidx.recyclerview.widget.RecyclerView>
    </androidx.coordinatorlayout.widget.CoordinatorLayout>
```

#### Work with header and footer view 

Below is an example from the sample application, notice that there is an anchored view
that is a feature of CoordinatorLayout which make the behaviors more powerful.

```xml
    <androidx.coordinatorlayout.widget.CoordinatorLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/coordinator_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.androidpi.literefresh.sample.ui.widget.SampleHeaderView
            android:id="@+id/view_header"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:layout_behavior="@string/lr_refresh_header_behavior"
            app:lr_maxOffsetRatio="100%p"
            app:lr_mode="follow"
            app:lr_visibleHeightRatio="50%" />

        <com.androidpi.literefresh.sample.ui.widget.SampleFooterView
            android:id="@+id/view_footer"
            android:layout_width="match_parent"
            android:layout_height="150dp"
            app:layout_behavior="@string/lr_refresh_footer_behavior"
            app:lr_maxOffsetRatio="100%p" />

        <com.androidpi.literefresh.sample.ui.widget.SampleContentView
            android:id="@+id/view_content"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:lr_minOffset="64dp"
            app:layout_behavior="@string/lr_refresh_content_behavior"/>

        <com.androidpi.literefresh.sample.ui.widget.SampleAnchoredView
            android:id="@+id/view_anchored"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:layout_anchor="@id/view_content"
            app:layout_anchorGravity="top|center_horizontal" />
    </androidx.coordinatorlayout.widget.CoordinatorLayout>
```

### Attach behaviors from code
Unlike attach behavior in the layout that you can configure the behavior with attributes.
Add behavior through code has some limitations that the configuration is not supported right now, 
but will be supported soon in a later version.
 
#### Set behaviors in `CoordinatorLayout.LayoutParams`
When using this method you must be sure the layout params has been generated. 

```java
    CoordinatorLayout.LayoutParams params = ((CoordinatorLayout.LayoutParams) getLayoutParams());
    RefreshHeaderBehavior headerBehavior = new RefreshHeaderBehavior(context);
    params.setBehavior(headerBehavior);
```
#### Implement interface `AttachedBehavior` for custom view
If you implement a custom view and want to attach a behavior you can implement the
`AttachedBehavior` interface.

```java
public class RefreshHeaderLayout extends FrameLayout implements CoordinatorLayout.AttachedBehavior{

    protected RefreshHeaderBehavior behavior;
    
    // ...

    public RefreshHeaderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        behavior = new RefreshHeaderBehavior(context, attrs);
    }

    @NonNull
    @Override
    public CoordinatorLayout.Behavior getBehavior() {
        return behavior;
    }
}
```

## Supported nested scrolling view
The supported nested scrolling view for content are list below.

 View               | API level 
--------------------|-------------------
 NestedScrollingView| 7 
 RecyclerView       | 7
 ListView           | 21

## Download

```gradle
dependencies {
    implementation 'com.androidpi:literefresh:0.9.1-alpha'
    implementation 'com.androidpi:literefresh-widgets:0.9.1-alpha'
}
```

## Licence

    Copyright 2018 yinpinjiu@gmail.com
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
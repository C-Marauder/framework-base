
# 1.模块简介
**ViewModel+LiveData+Retrofit2+Realm+RxKotlin 建议使用单个Activity+多Fragment开发**

## 使用Kotlin基于Androidx的MVVM开发框架,每个模块都是独立的，可自由组装。目前分为四个模块：

###  [1.基础模块](https://github.com/xqy666666/UI)

#### 基础模块包含下面几个小模块：

* [UI模块](https://github.com/xqy666666/framework)

* [动态权限模块](https://github.com/xqy666666/Kotlin-Permission)

* [动态改变状态栏模块](https://github.com/xqy666666/Kotlin-StausBarUtils)

* [下载模块](https://github.com/xqy666666/Downloader)

###  [2.跨组件通信模块](https://github.com/xqy666666/EventManager)

###  [3.核心模块](https://github.com/xqy666666/Framework-core)

###  [4.数据库模块](https://github.com/xqy666666/realm)

### [UI模块简介](https://github.com/xqy666666/UI)

[ ![Download](https://api.bintray.com/packages/xqy666666/maven/framework-base/images/download.svg?version=1.0.2) ](https://bintray.com/xqy666666/maven/framework-base/1.0.2/link)

#### 1.依赖

`implementation 'com.xqy.androidx.framework:framework-base:1.0.2'`

**2.功能介绍**

**1.UI模板全局配置**

```
class App:Application() {

    override fun onCreate() {
        super.onCreate()

        UIConfig.Builder {
            titleColor {  }//toolbar标题的颜色
            titleSize {  }//toolbar标题的字体大小
            navIcon {  }//导航icon
            clearElevation {  }//toolbar是否需要阴影
        }
    }
}
```

**2.实现UITemplate接口，生成UI模板**

* 在activity中（必须继承AppCompatActivity）
```
class MainActivity : AppCompatActivity(),UITemplate {//让activity实现UITemplate接口
    
    override val mScaffold: Boolean = false//true 基于CoordinatorLayout模板，false基于ConstraintLayout
    override val centerTitle: String = "Main" //toolbar居中标题
    override val layoutResId: Int = R.layout.activity_main//内容视图
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createContentView())//调用createContentView()
        
    }
}
```
* 在fragment中

```
class UIFragment:Fragment(),UITemplate {
    override val layoutResId: Int
    override val centerTitle: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createContentView()
    }


}

```
<img src="https://github.com/xqy666666/UI/blob/master/template1.png" width="200" height="400" alt="模板UI样式"/>

**3.使用DataBinding-继承MVVMFragment**

```
class MyFragment: MVVMFragment<FMyBinding>() {
    override val layoutResId: Int by lazy {
        R.layout.f_my
    }
    override val centerTitle: String = "MyFragment"
    //如需处理物理返回事件，可以重写该方法
    override val onBackPressed: (() -> Unit)?={
        Log.e("====","===")
    }
}

```
> **如需在宿主Activity中回调事件，可让Activity实现FragmentCallback接口。在需要的地方调用 FragmentCallback.onResponse()方法即可。**

**4.网络状态监测**

> **只需要在Activity中调用NetWorkManager.run()**

```
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //state-网络状态（LOST:断开,UNAVAILABLE:连接不到网络,CONNECTED:连接）
        NetWorkManager.run(application){state, mNetWorkInfo->
            when(state){
                NetWorkState.CONNECTED-> {
                    mNetWorkInfo?.let {

                        alertMsg("网络已连接！"){}
                    }
                    UIStateManager.changeUIState("MainActivity",UIState.DEFAULT)
                }
                NetWorkState.LOST->alertMsg("网络开小差了！"){}
            }
        }
}
```
<img src="https://github.com/xqy666666/UI/blob/master/network.gif" width="200" height="400" alt="网络监听"/>

**5.实现UIStateCallback接口,添加状态View**

> **UIState-LOADING(加载状态),EMPTY(空状态),DEFAULT(默认正常状态),在需要改变状态的逻辑处调用                    UIStateManager.changeUIState()方法，mUIStateKey:Activity or Fragmnet的名字，state:UIState**

```
class MainActivity : AppCompatActivity(),UITemplate, UIStateCallback {
    override val mUnConnectedResId: Int by lazy {//无网络状态，第一次进入页面时会设置
        R.drawable.ic_network_error
    }
    override val mEmptyResId: Int by lazy {//空状态
        R.drawable.ic_empty
    }
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
        NetWorkManager.run(application){
                state, mNetWorkInfo->
            when(state){
                NetWorkState.CONNECTED-> {
                    
                    UIStateManager.changeUIState("MainActivity",UIState.DEFAULT)
                }
                NetWorkState.LOST->UIStateManager.changeUIState("MainActivity",UIState.EMPTY)
            }
        }
        setContentView(createContentView())
    }
}
class MyFragment : Fragment(),UITemplate, UIStateCallback {
    override val mUnConnectedResId: Int by lazy {
        R.drawable.ic_network_error
    }
    override val mEmptyResId: Int by lazy {
        R.drawable.ic_empty
    }
}

```
<img src="https://github.com/xqy666666/UI/blob/master/uiState.gif" width="200" height="400" alt="状态监听"/>

**6.对话框**

```
  val dialog = AppDialog.newBuilder()
            //.handleListener { 
              //  view ->  //在此方法中处理各种事件
            //}
            .isDefaultConfirmType(true, DialogConfig(16f,
                16f,R.color.colorAccent,
                R.color.colorPrimaryDark,
                android.R.color.black,
                {
                  
                },{
                  true  
                }))
            .isCancel()//是否允许点击外部消失
            //.layoutResId()//对话框内容视图
            .setAlpha()//背景透明度
            .dialogHeight()//设置高度
            .dialogWidth()//设置宽度
            .dialogRadius()//设置圆角大小,直接给数值,单位默认为dp
            .build()
  dialog.show(supportFragmentManager,"")//显示
```
**7.列表**

> **列表是基于阿里的vLayout封装的**

```
 val delegateAdapter = mRecyclerView.initRecyclerView()//初始化参数
        for (i in 0..20){
            val adapter = AppAdapter<String>("3",{
                R.layout.item
            },{
                MyViewHolder({

                    loadingFragment.show(supportFragmentManager,"loading")
                    //startActivity(Intent(this@MainActivity,MyActivity::class.java))

                },it)
            }, LinearLayoutHelper())
            delegateAdapter.addAdapter(adapter)

 }
 或者
  val items = arrayListOf<String>()
        for (i in 0 ..20){
            items.add("$i")
        }
        val adapters = AppAdapter<String>(items,{
            R.layout.item
        },{
            MyViewHolder({

                loadingFragment.show(supportFragmentManager,"loading")
                //startActivity(Intent(this@MainActivity,MyActivity::class.java))

            },it)
        }, LinearLayoutHelper())
        delegateAdapter.addAdapter(adapters)
```
**[10.折叠列表](https://github.com/xqy666666/Kotlin-ExpandableRecyclerView)**

> **基于RecyclerView封装**

```
//调用RecyclerView的拓展方法
 mRecyclerView.initRV {
            val rv = it
            it.addItemDecoration(AppItemDecoration(8, 8, Color.GRAY))
            val items = mutableListOf<String>()
            for (i in 0..20) {
                items.add("$i")
            }
            it.adapter = ExpandableAdapter.builder {
                groupLayout {
                    R.layout.item
                }
                headerLayout {
                    R.layout.item_test
                }
                getHeaderItemData {
                    "我是头部"
                }
                bindHeaderItemData<String> {
                    item, dataBinding ->  (dataBinding as ItemTestBinding).num = item
                }
                childLayout {
                    R.layout.item_child
                }

                groupItems {
                    items
                }

                childItems { position ->
                    mutableListOf("hahah", "ppppp", "建设大街世纪东方")

                }
                bindGroupItemData<String> { item, dataBinding ->
                    (dataBinding as ItemBinding).num = item
                }



                createChildViewHolder {
                    dataBinding, itemView ->  ChildViewHolder(dataBinding as ItemChildBinding,itemView)
                }
            }
        }
```
 <img src="https://github.com/xqy666666/framework/blob/master/expand.gif" width="200" height="400" />

> 防重复点击-直接调用该方法

``` 
yourView.onClick{
    //doThings()
}

```
> **当UITemplate的参数mScaffold=false时，toolbar不会随列表滚动**

<div >
    <img src="https://github.com/xqy666666/UI/blob/master/list1.gif" width="200" height="400" alt="状态监听"/>
    <img src="https://github.com/xqy666666/UI/blob/master/list2.gif" width="200" height="400" alt="状态监听"/>
    <img src="https://github.com/xqy666666/UI/blob/master/list3.gif" width="200" height="400" alt="状态监听"/>
</div>

**9.本地缓存-SharedPreferences**

```
var mUserId:Int by AppPreference(application,123)
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUserId = 110//直接赋值，自动存储
  }
```

**10.数据加密**

* AES加密
```
//第一个参数是加密秘钥，第二个是加密内容
val result = SecurityHelper.mInstance.encryptByAES("123",content)

//解密
val deResult = SecurityHelper.mInstance.decryptByAES(result)

```

* RAS加密

**先在Application的onCreate()中进行初始化**

```
SecurityHelper.init(this)

```

```
//存储到本地
val encodeContent = SecurityHelper.mInstance.encryptToLocalByRsa("123456","test")
encodeView.text =encodeContent

//从本地读取
val decodeContent = SecurityHelper.mInstance.decryptFromLocalByRsa("test")
decodeView.text = decodeContent

//前后端交互
//1.从后端获取公钥保存
SecurityHelper.mInstance.saveBase64PubKey(publicKey)
//2.加密内容        
SecurityHelper.mInstance.encryptByRsa("111111")
```
<img src="https://github.com/xqy666666/UI/blob/master/rsa.gif" width="200" height="400"/>


### [动态权限模块简介](https://github.com/xqy666666/Kotlin-Permission)

### 1.依赖

[ ![Download](https://api.bintray.com/packages/xqy/maven/permission/images/download.svg?version=1.0.5) ](https://bintray.com/xqy/maven/permission/1.0.5/link)

`implementation 'com.xqy.androidx.permission:permission:1.0.5'`

**获取单个权限**
```
textView.setOnClickListener {
            PermissionHelper.from(this).requestPermission(Manifest.permission.CAMERA,
                hasPermission = { permission ->//该权限已获取

                    alertMsg("$permission 已经获取"){

                    }

                },
                observer = { permission, isGranted ->//请求权限的回调

                    val msg = if (isGranted)"成功" else "失败"
                    alertMsg("$permission 获取$msg"){

                    }

                    //toDoSomeThings
                })
        }
```
 <img src="https://github.com/xqy666666/UI/blob/master/permission.gif" width="200" height="400" alt="状态监听"/>
 
 **获取多个权限**
 
 ```
 textView.setOnClickListener {
            PermissionHelper.from(this).requestPermission(Manifest.permission.CAMERA,Manifest.permission.READ_PHONE_STATE,
                hasPermission = { permission ->//该权限已获取

                    alertMsg("$permission 已经获取"){

                    }

                },
                observer = { permission, isGranted ->//请求权限的回调

                    val msg = if (isGranted)"成功" else "失败"
                    alertMsg("$permission 获取$msg"){

                    }

                    //toDoSomeThings
                })
        }
 ```
 
### [动态状态栏模块简介](https://github.com/xqy666666/Kotlin-StausBarUtils)

### 1.依赖

`implementation 'com.xqy.android.statusbar:statusbarUtils:1.0.0'`

## 2.使用

```
在Activity的onCreate()方法中调用
//isDark true表示黑色，false表示白色
StatusBarUtils.setStatusBarMode(activity,isDark)

```

 <img src="https://github.com/xqy666666/framework/blob/master/statusbar.gif" width="200" height="400" />
 
### [下载模块简介](https://github.com/xqy666666/Downloader)

### 1.依赖

`implementation 'com.xqy.androidx.downloader:downloader:1.0.1'`


## [2.跨组件通信模块简介](https://github.com/xqy666666/EventManager)

### 1.依赖

`implementation 'com.xqy.androidx.event:event:1.0.1'`

### 2.使用

```
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView.setOnClickListener {
            startActivity(Intent(this,BActivity::class.java))
        }
        registerEvent<String>("a"){
            Log.e("==","=====")
            textView.text = it
            textView.textSize = 24f
        }
    }
}

class BActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.b_activity)
        textView.setOnClickListener {
            sendEvent("a","Hello MainActivity")
        }

    }

}
```
## [3.核心模块简介](https://github.com/xqy666666/Framework-core)





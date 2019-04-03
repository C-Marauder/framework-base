# UI
a android ui library
## 使用Kotlin基于Androidx开发的Android UI框架,建议使用单个Activity+多Fragment开发
## 1.依赖
`implementation 'com.androidx.ui:ui:1.0.1'`

## 2.使用
* UI模板
#### 1.activity（必须继承AppCompatActivity）
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
#### 2.fragment中
```
class UIFragment:Fragment(),UITemplate {
    override val layoutResId: Int
    override val centerTitle: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createContentView()
    }


}

```
<img src="https://github.com/xqy666666/UI/blob/master/template1.png" width="300" height="600" alt="模板UI样式"/>

* 网络状态监测
#### 只需要在Activity中调用NetWorkManager.run()
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
<img src="https://github.com/xqy666666/UI/blob/master/network.gif" width="300" height="600" alt="网络监听"/>
* 添加状态View

#### 让Activity or Fragment实现UIStateCallback接口
##### UIState-LOADING(加载状态),EMPTY(空状态),DEFAULT(默认正常状态),在需要改变状态的逻辑处调用                    UIStateManager.changeUIState()方法，mUIStateKey:Activity or Fragmnet的名字，state:UIState

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
<img src="https://github.com/xqy666666/UI/blob/master/uiState.gif" width="300" height="600" alt="状态监听"/>


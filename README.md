# UI
a android ui library
## 使用Kotlin基于Androidx开发的Android UI框架
## 1.依赖
`implementation 'com.androidx.ui:ui:1.0.1'`

## 2.使用
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
![image][https://github.com/xqy666666/UI/blob/master/template1.png]

class MainActivity : AppCompatActivity() {
    private lateinit var cosmoLoader: CosmoLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layout = findViewById<RelativeLayout>(R.id.main_layout)

        cosmoLoader = CosmoLoader.Builder(this)
            .setSpeed(1500L)
            .setMaxOffset(70f)
            .build()

        layout.addView(cosmoLoader)

        cosmoLoader.startAnimation()

        cosmoLoader.updateMessage("Almost done...")

        cosmoLoader.updateMessage("UpsIde Down")
    }

    override fun onStop() {
        super.onStop()
        cosmoLoader.stopAnimation()
    }
}

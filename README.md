// Creating the loader with the builder pattern
val loader = CosmoLoader.Builder(context)
    .setMessage("Please wait...")
    .setSpeed(1500L)
    .setMaxOffset(80f)
    .build()

// Starting the animation
loader.startAnimation()

// Updating the message dynamically
loader.updateMessage("Almost done...")

//Updating speed dynamically (Speed i n long in millisecond for loader from one end to other)
loader.updateSpeed(2500)

// Stopping the animation
loader.stopAnimation()

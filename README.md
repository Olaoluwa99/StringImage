# ImageID: Transform User IDs into Unique Profile Images

ImageID is an innovative Android library designed to simplify profile image generation for developers. By leveraging algorithms, ImageID transforms user-specific ID strings into visually distinct profile images, eliminating the need for traditional image storage methods. Prioritize user privacy, enhance app performance, and offer a personalized experience with ImageID.

## Features

- **Efficient Image Generation:** ImageID eliminates the need for storing individual profile pictures, significantly reducing storage requirements and enhancing app performance.
  
- **Privacy-Focused:** Personal images are not uploaded or stored, ensuring user data remains secure.

- **Customization Options:** Developers can tailor ImageID to fit their app's aesthetic preferences and branding guidelines, offering a personalized experience for users.

- **Scalable Solution:** ImageID is optimized for scalability, making it suitable for apps with varying user bases and usage patterns.

## How to Use

### Integration

1. Add the JitPack repository to your project's `build.gradle` file:

```groovy
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		maven { url 'https://jitpack.io' }
	}
}
```

2. Add the dependency to your project's `build.gradle` file:

```groovy
dependencies {
	implementation 'com.github.Olaoluwa99:StringImage:1.0.0'
}
```

3. Initialize ImageID in your app:

```kotlin
val imageId = ImageId()
```

#### Creating ImageID
```kotlin
//Create ImageID
imageId.createUserId(
        mainText = "Hello",
        startColorRed = 200, startColorGreen = 124, startColorBlue = 004,
        endColorRed = 024, endColorGreen = 123, endColorBlue = 094,
        userId = 123456789,
        shuffleSeed = 123456L
)
//Input 'Rgb' values for colors to generate ID string.
```

#### Generating Profile Image
```kotlin
//Generate bitmap from ID
val bitmap = imageId.getImageFromId(imageId)
val profileImage = ImageID.generateProfileImage(userID)
imageView.setImageBitmap(profileImage)
```

#### Retrieving ID values
```kotlin
//Retrieve values from Id
val result: Task<Text> = imageId.retrieveIdByImage(userImage: Bitmap)

val fullText = ""
val result = idImage.retrieveIdByImage(profileImageBitmap)

result.addOnSuccessListener {
	it.textBlocks[0].text	//Main Text
	it.textBlocks[1].text	//UserId
	it.textBlocks[2].text	//User color code - (rgbStart - rgbEnd)
	it.textBlocks[3].text	//User shuffleSeed

	//Full text on Image
	for (block in it.textBlocks) {
		val blockText = block.text
		testResult = "$fullText$blockText".replace(" ", "")
	}
}

```


## Use Cases

- **Social Media Platforms:** Enhance user privacy while maintaining a visually appealing interface with unique profile images on social networking apps.
  
- **Dating Apps:** Provide users with distinctive avatars without compromising their privacy or requiring photo uploads.

- **E-commerce Platforms:** Streamline user registration and enhance privacy by generating unique avatars for e-commerce customers.

## Contributing

Contributions are welcome! Please see our [Contributing Guidelines](CONTRIBUTING.md) for more details.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

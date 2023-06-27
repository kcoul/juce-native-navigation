/*
  ==============================================================================

    This file was auto-generated!

  ==============================================================================
*/

#include "MainComponent.h"

//==============================================================================
MainContentComponent::MainContentComponent()
{
    setSize (600, 400);

    title = "title";

    message = "message";

    // Set up some initial dummy data (courtesy of http://www.json-generator.com)
    data = Data(String("[\n"
                               "  {\n"
                               "    \"title\": \"Radiantix\",\n"
                               "    \"message\": \"Occaecat ullamco amet ea sit ex incididunt enim.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Imaginart\",\n"
                               "    \"message\": \"Officia eiusmod laborum qui irure tempor velit qui anim ullamco pariatur veniam occaecat ex.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Futurize\",\n"
                               "    \"message\": \"Excepteur nulla ex sit cupidatat aute exercitation veniam.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Entogrok\",\n"
                               "    \"message\": \"Esse ex id incididunt cillum eiusmod est sint proident consectetur.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Mitroc\",\n"
                               "    \"message\": \"Deserunt proident adipisicing commodo aliquip irure quis non do nostrud labore ex officia esse.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Architax\",\n"
                               "    \"message\": \"Eiusmod mollit duis esse magna ipsum pariatur minim quis ea cupidatat reprehenderit aliqua ipsum.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Geekmosis\",\n"
                               "    \"message\": \"Duis exercitation fugiat id sit ipsum adipisicing cupidatat irure anim.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Baluba\",\n"
                               "    \"message\": \"Commodo ut proident et excepteur ut ex ex.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Zillactic\",\n"
                               "    \"message\": \"Non exercitation deserunt aliquip enim deserunt ut fugiat voluptate quis esse fugiat aute cillum laborum.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Dragbot\",\n"
                               "    \"message\": \"Consectetur non eu in irure Lorem consequat.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Qualitern\",\n"
                               "    \"message\": \"Nisi enim anim commodo ea ullamco magna.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Hinway\",\n"
                               "    \"message\": \"Quis voluptate ex eu culpa nisi pariatur anim.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Hairport\",\n"
                               "    \"message\": \"Culpa sunt eu excepteur ullamco excepteur incididunt esse ut amet dolore elit duis mollit.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Zolarity\",\n"
                               "    \"message\": \"Irure sit laboris adipisicing consequat ea dolor pariatur ullamco.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Accidency\",\n"
                               "    \"message\": \"Minim ut eu nostrud esse ex labore.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Quadeebo\",\n"
                               "    \"message\": \"Reprehenderit minim Lorem proident tempor.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Pathways\",\n"
                               "    \"message\": \"Reprehenderit fugiat velit est in fugiat labore dolor qui reprehenderit ad aute.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Solgan\",\n"
                               "    \"message\": \"Nisi ut occaecat duis mollit eiusmod exercitation pariatur qui ea.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Buzzworks\",\n"
                               "    \"message\": \"Qui irure sint occaecat cillum aute est nisi ipsum fugiat duis.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Senmei\",\n"
                               "    \"message\": \"Aliqua aliquip nulla adipisicing reprehenderit tempor officia minim.\"\n"
                               "  },\n"
                               "  {\n"
                               "    \"title\": \"Mangelica\",\n"
                               "    \"message\": \"Pariatur ipsum qui eu sunt duis excepteur ipsum.\"\n"
                               "  }\n"
                               "]"));
}

MainContentComponent::~MainContentComponent()
{

}

void MainContentComponent::paint (Graphics& g)
{
    g.fillAll (Colour (0xff001F36));

    g.setFont (Font (24.0f));
    g.setColour (Colours::orange);
    //String title(data.getValueTree().getChild(0).getProperty(Ids::title).toString());
    g.drawText (title, getLocalBounds(), Justification::centredTop, true);

    g.setFont (Font (16.0f));
    g.setColour (Colours::white);
    //String message(data.getValueTree().getChild(0).getProperty(Ids::message).toString());
    g.drawText (message, getLocalBounds(), Justification::centred, true);
}

void MainContentComponent::resized()
{
    // This is called when the MainContentComponent is resized.
    // If you add any child components, this is where you should
    // update their positions.
}

#if JUCE_ANDROID

static void JNIEXPORT testCallbackJni()
{
    int i = 1;
}

#define JNI_CLASS_MEMBERS(METHOD, STATICMETHOD, FIELD, STATICFIELD, CALLBACK) //\
     //CALLBACK (testCallbackJni, "testCallback", "()V)") //\                                                                         \
     //CALLBACK (getJsonDataStringJni, "getJsonDataString", "(V)Ljava/lang/String;") \
     //CALLBACK (setMessageJni,        "setMessage",        "(Ljava/lang/String;)V")

DECLARE_JNI_CLASS (NativeNavigationJuceActivityJavaClass, "com/codegarden/nativenavigation/JuceActivity")
#undef JNI_CLASS_MEMBERS

static jstring JNIEXPORT getJsonDataStringJni(JNIEnv* env, jobject javaInstance)
{
    SharedResourcePointer<MainContentComponent> mainComponent;
    String jsonData = mainComponent->data.toJson();
    return javaString(jsonData);
}

static void JNIEXPORT setMessageJni(JNIEnv* env, jobject javaInstance, jstring message)
{
    SharedResourcePointer<MainContentComponent> mainComponent;

    mainComponent->message = juceString (env, message);
    mainComponent->repaint();
}

static jbyteArray getJsonDataBytes()
{
    auto* env = getEnv();

    SharedResourcePointer<MainContentComponent> mainComponent;
    String jsonData = mainComponent->data.toJson();

    DBG (jsonData);

    int byteCount = jsonData.length();
    const jbyte* nativeString = reinterpret_cast<const jbyte*> ((const char *) jsonData.toUTF8());
    jbyteArray bytes = env->NewByteArray(byteCount);
    env->SetByteArrayRegion(bytes, 0, byteCount, nativeString);

    return bytes;
}
#endif

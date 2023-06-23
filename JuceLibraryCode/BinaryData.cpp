/* ==================================== JUCER_BINARY_RESOURCE ====================================

   This is an auto-generated file: Any edits you make may be overwritten!

*/

#include <cstring>

namespace BinaryData
{

//================== MasterViewController.xib ==================
static const unsigned char temp_binary_data_0[] =
"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
"<document type=\"com.apple.InterfaceBuilder3.CocoaTouch.XIB\" version=\"3.0\" toolsVersion=\"9060\" systemVersion=\"15B42\" targetRuntime=\"iOS.CocoaTouch\" propertyAccessControl=\"none\" useAutolayout=\"YES\" useTraitCollections=\"YES\">\n"
"    <dependencies>\n"
"        <deployment identifier=\"iOS\"/>\n"
"        <plugIn identifier=\"com.apple.InterfaceBuilder.IBCocoaTouchPlugin\" version=\"9051\"/>\n"
"    </dependencies>\n"
"    <objects>\n"
"        <placeholder placeholderIdentifier=\"IBFilesOwner\" id=\"-1\" userLabel=\"File's Owner\" customClass=\"MasterViewController\" customModule=\"NativeNavigation\" customModuleProvider=\"target\">\n"
"            <connections>\n"
"                <outlet property=\"view\" destination=\"DD9-1X-2F6\" id=\"9GI-AR-meM\"/>\n"
"            </connections>\n"
"        </placeholder>\n"
"        <placeholder placeholderIdentifier=\"IBFirstResponder\" id=\"-2\" customClass=\"UIResponder\"/>\n"
"        <view clearsContextBeforeDrawing=\"NO\" contentMode=\"scaleToFill\" id=\"i5M-Pr-FkT\">\n"
"            <rect key=\"frame\" x=\"0.0\" y=\"0.0\" width=\"600\" height=\"600\"/>\n"
"            <autoresizingMask key=\"autoresizingMask\" widthSizable=\"YES\" heightSizable=\"YES\"/>\n"
"            <subviews>\n"
"                <label opaque=\"NO\" userInteractionEnabled=\"NO\" contentMode=\"left\" horizontalHuggingPriority=\"251\" verticalHuggingPriority=\"251\" fixedFrame=\"YES\" text=\"Messages\" lineBreakMode=\"tailTruncation\" baselineAdjustment=\"align"
"Baselines\" adjustsFontSizeToFit=\"NO\" translatesAutoresizingMaskIntoConstraints=\"NO\" id=\"Y4d-5V-Qkc\">\n"
"                    <rect key=\"frame\" x=\"183\" y=\"13\" width=\"78\" height=\"21\"/>\n"
"                    <animations/>\n"
"                    <fontDescription key=\"fontDescription\" type=\"system\" pointSize=\"17\"/>\n"
"                    <color key=\"textColor\" red=\"0.0\" green=\"0.0\" blue=\"0.0\" alpha=\"1\" colorSpace=\"calibratedRGB\"/>\n"
"                    <nil key=\"highlightedColor\"/>\n"
"                </label>\n"
"                <tableView clipsSubviews=\"YES\" contentMode=\"scaleToFill\" fixedFrame=\"YES\" alwaysBounceVertical=\"YES\" style=\"plain\" separatorStyle=\"default\" rowHeight=\"44\" sectionHeaderHeight=\"22\" sectionFooterHeight=\"22\" translatesA"
"utoresizingMaskIntoConstraints=\"NO\" id=\"DD9-1X-2F6\">\n"
"                    <rect key=\"frame\" x=\"0.0\" y=\"71\" width=\"600\" height=\"300\"/>\n"
"                    <animations/>\n"
"                    <color key=\"backgroundColor\" white=\"1\" alpha=\"1\" colorSpace=\"calibratedWhite\"/>\n"
"                    <connections>\n"
"                        <outlet property=\"dataSource\" destination=\"Q9f-cg-t3e\" id=\"sFO-iy-dOw\"/>\n"
"                        <outlet property=\"delegate\" destination=\"-1\" id=\"08j-dz-Mgn\"/>\n"
"                    </connections>\n"
"                </tableView>\n"
"            </subviews>\n"
"            <animations/>\n"
"            <color key=\"backgroundColor\" white=\"1\" alpha=\"1\" colorSpace=\"custom\" customColorSpace=\"calibratedWhite\"/>\n"
"        </view>\n"
"        <customObject id=\"Q9f-cg-t3e\" customClass=\"CBLUITableSource\"/>\n"
"    </objects>\n"
"</document>\n";

const char* MasterViewController_xib = (const char*) temp_binary_data_0;


const char* getNamedResource (const char* resourceNameUTF8, int& numBytes);
const char* getNamedResource (const char* resourceNameUTF8, int& numBytes)
{
    unsigned int hash = 0;

    if (resourceNameUTF8 != nullptr)
        while (*resourceNameUTF8 != 0)
            hash = 31 * hash + (unsigned int) *resourceNameUTF8++;

    switch (hash)
    {
        case 0x1eb62c95:  numBytes = 2976; return MasterViewController_xib;
        default: break;
    }

    numBytes = 0;
    return nullptr;
}

const char* namedResourceList[] =
{
    "MasterViewController_xib"
};

const char* originalFilenames[] =
{
    "MasterViewController.xib"
};

const char* getNamedResourceOriginalFilename (const char* resourceNameUTF8);
const char* getNamedResourceOriginalFilename (const char* resourceNameUTF8)
{
    for (unsigned int i = 0; i < (sizeof (namedResourceList) / sizeof (namedResourceList[0])); ++i)
        if (strcmp (namedResourceList[i], resourceNameUTF8) == 0)
            return originalFilenames[i];

    return nullptr;
}

}

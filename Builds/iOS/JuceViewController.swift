//
//  RootViewController.swift
//  JustTuner
//
//  Created by Adam Elemental on 08/09/2015.
//
//

import UIKit

class JuceViewController: UIViewController {
    
    var navBar: UINavigationBar!
    var dataController: DataControllerObjC
    
    @objc var contentView: UIView!
    
    @objc init (contentView: UIView) {
        self.contentView = contentView
        dataController = DataControllerObjC()
        super.init(nibName: nil, bundle:nil)
        self.view.addSubview(self.contentView)
    }

    required convenience init(coder aDecoder: NSCoder) {
        self.init(coder: aDecoder)
    }
    
    override func loadView() {
        super.loadView()
        let navHeight: CGFloat = 32.0
        setTitle()

        if contentView == nil {
            contentView = UIView(frame: CGRect(x: 0,
                                               y: navHeight,
                                               width: self.view.bounds.size.width,
                                               height: self.view.bounds.size.height - navHeight))
            contentView.backgroundColor = UIColor.black
        }
    }
    
    func setTitle() {
        if let title = dataController.getTitle() {
            self.navigationItem.title = title;
        } else {
            self.navigationItem.title = "Untitled"
        }
    }
    
//    override func dealloc() {
//        self.contentView.release()
//        self.navBar.release()
//        super.dealloc()
//    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

}

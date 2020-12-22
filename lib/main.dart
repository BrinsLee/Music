import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:light_music_flutter/models/themeModel.dart';
import 'package:light_music_flutter/routes/bloc_wrapper.dart';
import 'package:light_music_flutter/routes/find_page.dart';
import 'package:light_music_flutter/routes/home_page.dart';
import 'package:light_music_flutter/routes/mine_page.dart';
import 'package:light_music_flutter/routes/video_page.dart';
import 'package:provider/provider.dart';

SystemUiOverlayStyle uiStyle = SystemUiOverlayStyle.light.copyWith(
  statusBarColor: Colors.white,
);

void main() {
  SystemChrome.setSystemUIOverlayStyle(uiStyle);
  runApp(BlocWrapper(child: MyApp()));
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'LightMusic',
      debugShowCheckedModeBanner: false,
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final pages = [HomePage(), FindPage(), VideoPage(), MinePage()];
  final List<BottomNavigationBarItem> bottomNavItems = [
    BottomNavigationBarItem(
        icon: Image.asset(
          "images/tab_home_normal.png",
          width: 20,
          height: 20,
        ),
        activeIcon: Image.asset(
          "images/tab_home_press.png",
          width: 20,
          height: 20,
        ),
        label: "首页"),
    BottomNavigationBarItem(
        icon: Image.asset(
          "images/tab_find_normal.png",
          width: 20,
          height: 20,
        ),
        activeIcon: Image.asset(
          "images/tab_find_press.png",
          width: 20,
          height: 20,
        ),
        label: "发现"),
    BottomNavigationBarItem(
        icon: Image.asset(
          "images/tab_activity_normal.png",
          width: 20,
          height: 20,
        ),
        activeIcon: Image.asset(
          "images/tab_activity_press.png",
          width: 20,
          height: 20,
        ),
        label: "视频"),
    BottomNavigationBarItem(
        icon: Image.asset(
          "images/tab_my_normal.png",
          width: 20,
          height: 20,
        ),
        activeIcon: Image.asset(
          "images/tab_my_press.png",
          width: 20,
          height: 20,
        ),
        label: "我的")
  ];
  int _currentIndex = 0;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
          primaryColor: Colors.white,
          accentColor: Colors.black,
          iconTheme: IconThemeData(color: Colors.lightBlue)),
      home: Scaffold(
        bottomNavigationBar: BottomNavigationBar(
          items: bottomNavItems,
          currentIndex: _currentIndex,
          type: BottomNavigationBarType.fixed,
          selectedItemColor: Colors.black,
          selectedFontSize: 12,
          unselectedItemColor: Colors.grey,
          onTap: (index) {
            setState(() {
              if (index != _currentIndex) {
                _currentIndex = index;
              }
            });
          },
        ),
        body: pages[_currentIndex],
      ),
    );
  }
}

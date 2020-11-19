import 'package:flutter/material.dart';
import 'package:light_music_flutter/models/themeModel.dart';
import 'package:light_music_flutter/routes/find_page.dart';
import 'package:light_music_flutter/routes/home_page.dart';
import 'package:light_music_flutter/routes/mine_page.dart';
import 'package:light_music_flutter/routes/video_page.dart';
import 'package:provider/provider.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
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
        title: Text("首页")),
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
        title: Text("发现")),
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
        title: Text("视频")),
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
        title: Text("我的"))
  ];
  int _currentIndex = 0;
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: <SingleChildCloneableWidget>[
        ChangeNotifierProvider.value(value: ThemeModel()),
      ],
      child: Consumer<ThemeModel>(
          builder: (BuildContext context, themeModel, Widget child) {
        return MaterialApp(
          theme: ThemeData(
              primaryColor: Colors.white,
              accentColor: Colors.black,
              iconTheme: IconThemeData(color: Colors.lightBlue)),
          home: Scaffold(
            bottomNavigationBar: BottomNavigationBar(
              items: bottomNavItems,
              currentIndex: _currentIndex,
              type: BottomNavigationBarType.shifting,
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
      }),
    );
  }
}

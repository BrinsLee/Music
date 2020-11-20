import 'package:flutter/material.dart';
import 'package:light_music_flutter/net/api/api_service.dart';
import 'package:light_music_flutter/widget/banner_widget.dart';
import 'package:light_music_flutter/widget/daily_recommend_widget.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "LightMusic",
          style: TextStyle(
            color: Colors.black,
            fontWeight: FontWeight.bold,
          ),
        ),
        elevation: 0,
      ),
      body: Column(
        children: [
          BannerWidget(),
          Container(
            margin: EdgeInsets.all(10),
            child: DailyRecommend(),
          ),
          Container(
            margin: EdgeInsets.only(left: 10, bottom: 10),
            alignment: Alignment.centerLeft,
            child: Text(
              "根据你喜欢歌曲推荐",
              style:
                  TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
            ),
          )
        ],
      ),
    );
  }
}

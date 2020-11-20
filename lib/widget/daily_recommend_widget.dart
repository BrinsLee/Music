import 'package:flutter/material.dart';
import 'package:light_music_flutter/widget/daily_item_widget.dart';

class DailyRecommend extends StatefulWidget {
  @override
  _DailyRecommendState createState() => _DailyRecommendState();
}

class _DailyRecommendState extends State<DailyRecommend> {
  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      children: [
        DailyItem("images/home_ic_daily.png", "每日推荐", () {
          print("点击每日推荐");
        }),
        DailyItem("images/home_ic_heart.png", "心动模式", () {
          print("点击心动模式");
        }),
        DailyItem("images/home_ic_rank.png", "排行榜", () {
          print("点击排行榜");
        }),
        DailyItem("images/home_ic_fm.png", "电台", () {
          print("点击电台");
        })
      ],
    );
  }
}

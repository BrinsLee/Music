import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:light_music_flutter/blocs/home/home_bloc.dart';
import 'package:light_music_flutter/net/api/api_service.dart';
import 'package:light_music_flutter/widget/banner_widget.dart';
import 'package:light_music_flutter/widget/daily_recommend_widget.dart';
import 'package:light_music_flutter/widget/loading_widget.dart';
import 'package:light_music_flutter/widget/personailzed_musiclist_widget.dart';

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
        body: BlocBuilder<HomeBloc, HomeState>(
          builder: (_, state) {
            return SingleChildScrollView(child: _buildContent(state));
          },
        ));
  }

  Widget _buildContent(HomeState state) {
    if (state is HomeLoading) {
      return LoadingWidget();
    }
    if (state is HomeDataLoaded) {
      List<dynamic> items = state.data;
      if (items.isEmpty) {
        return Column(
          children: [
            BannerWidget(items[0]),
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
            ),
            Container(
              child: PersonailzedMusicListWidget(),
            ),
            Container(
              margin: EdgeInsets.only(left: 10, bottom: 10),
              alignment: Alignment.centerLeft,
              child: Text(
                "晚霞灿烂，音乐惬意",
                style:
                TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
              ),
            ),
          ],
        );
      }
    }
  }
}

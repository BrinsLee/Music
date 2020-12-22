import 'package:flutter/material.dart';
import 'package:light_music_flutter/models/banner/banner_result.dart';
import 'package:light_music_flutter/models/personalizedmusiclist/personalized_music_list_result.dart';

/// Created by lipeilin
/// on 2020/12/21

abstract class HomeWidgetRepository {

  Future<BannerResult> loadBanner({int type = 1});

  Future<PersonalizedMusicListResult> loadPersonalizedMusicList(
      {int limit = 10});
}

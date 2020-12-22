import 'package:flutter/material.dart';
import 'package:light_music_flutter/models/banner/banner_result.dart';
import 'package:light_music_flutter/models/personalizedmusiclist/personalized_music_list_result.dart';
import 'package:light_music_flutter/net/api/api_service.dart';
import 'package:light_music_flutter/repositories/itf/home_repository.dart';

/// Created by lipeilin 
/// on 2020/12/21
class HomeNetRepository extends HomeWidgetRepository{

  @override
  Future<BannerResult> loadBanner({int type = 1}) {
    return ApiService().getBanner();
  }

  @override
  Future<PersonalizedMusicListResult> loadPersonalizedMusicList({int limit = 10}) {

  }

}
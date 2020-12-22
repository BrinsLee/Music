import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';
import 'package:light_music_flutter/models/personalizedmusiclist/personalized_music_list.dart';

/// Created by lipeilin 
/// on 2020/12/21
part 'personalized_music_list_result.g.dart';

@JsonSerializable()
class PersonalizedMusicListResult {

  int code = 0;
  int category = 0;

  List<PersonalizedMusicList> result;

  PersonalizedMusicListResult(this.code, this.category, this.result);

  factory PersonalizedMusicListResult.fromJson(Map<String, dynamic> json) =>
      _$PersonalizedMusicListResultFromJson(json);

  Map<String, dynamic> toJson() => _$PersonalizedMusicListResultToJson(this);
}
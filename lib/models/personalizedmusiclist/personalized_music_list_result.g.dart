// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'personalized_music_list_result.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

PersonalizedMusicListResult _$PersonalizedMusicListResultFromJson(
    Map<String, dynamic> json) {
  return PersonalizedMusicListResult(
      json['code'] as int,
      json['category'] as int,
      (json['result'] as List)
          ?.map((e) => e == null
              ? null
              : PersonalizedMusicList.fromJson(e as Map<String, dynamic>))
          ?.toList());
}

Map<String, dynamic> _$PersonalizedMusicListResultToJson(
        PersonalizedMusicListResult instance) =>
    <String, dynamic>{
      'code': instance.code,
      'category': instance.category,
      'result': instance.result
    };

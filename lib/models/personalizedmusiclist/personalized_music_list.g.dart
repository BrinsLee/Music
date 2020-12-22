// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'personalized_music_list.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

PersonalizedMusicList _$PersonalizedMusicListFromJson(
    Map<String, dynamic> json) {
  return PersonalizedMusicList(
      json['alg'] as String,
      json['canDisLike'] as bool,
      json['copywriter'] as String,
      json['highQuality'] as bool,
      json['id'] as int,
      json['name'] as String,
      json['picUrl'] as String,
      json['playCount'] as int,
      json['trackCount'] as int,
      json['trackNumberUpdateTime'] as int,
      json['type'] as int);
}

Map<String, dynamic> _$PersonalizedMusicListToJson(
        PersonalizedMusicList instance) =>
    <String, dynamic>{
      'id': instance.id,
      'type': instance.type,
      'name': instance.name,
      'copywriter': instance.copywriter,
      'picUrl': instance.picUrl,
      'canDisLike': instance.canDisLike,
      'trackNumberUpdateTime': instance.trackNumberUpdateTime,
      'playCount': instance.playCount,
      'trackCount': instance.trackCount,
      'highQuality': instance.highQuality,
      'alg': instance.alg
    };

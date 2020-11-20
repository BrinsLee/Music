// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'banner.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Banner _$BannerFromJson(Map<String, dynamic> json) {
  return Banner(
      json['pic'] as String,
      json['targetId'] as int,
      json['titleColor'] as String,
      json['url'] as String,
      json['typeTitle'] as String);
}

Map<String, dynamic> _$BannerToJson(Banner instance) => <String, dynamic>{
      'pic': instance.picUrl,
      'targetId': instance.targetId,
      'titleColor': instance.titleColor,
      'url': instance.url,
      'typeTitle': instance.typeTitle
    };

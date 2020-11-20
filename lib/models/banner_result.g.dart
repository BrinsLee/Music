// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'banner_result.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

BannerResult _$BannerResultFromJson(Map<String, dynamic> json) {
  return BannerResult((json['banners'] as List)
      ?.map(
          (e) => e == null ? null : Banner.fromJson(e as Map<String, dynamic>))
      ?.toList());
}

Map<String, dynamic> _$BannerResultToJson(BannerResult instance) =>
    <String, dynamic>{'banners': instance.banners};

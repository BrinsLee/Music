import 'package:json_annotation/json_annotation.dart';
import 'package:light_music_flutter/models/banner.dart';

part 'banner_result.g.dart';

@JsonSerializable()
class BannerResult {
  BannerResult(this.banners);
  List<Banner> banners;

  factory BannerResult.fromJson(Map<String, dynamic> json) => _$BannerResultFromJson(json);

  Map<String, dynamic> toJson() => _$BannerResultToJson(this);
}

import 'package:json_annotation/json_annotation.dart';

part 'personalized_music_list.g.dart';

@JsonSerializable()
class PersonalizedMusicList {
  int id;

  int type = 0;

  String name;

  String copywriter;

  String picUrl;

  bool canDisLike = false;

  int trackNumberUpdateTime;

  int playCount = 0;

  int trackCount = 0;

  bool highQuality = false;

  String alg;

  PersonalizedMusicList(
      this.alg,
      this.canDisLike,
      this.copywriter,
      this.highQuality,
      this.id,
      this.name,
      this.picUrl,
      this.playCount,
      this.trackCount,
      this.trackNumberUpdateTime,
      this.type);

  factory PersonalizedMusicList.fromJson(Map<String, dynamic> json) =>
      _$PersonalizedMusicListFromJson(json);

  Map<String, dynamic> toJson() => _$PersonalizedMusicListToJson(this);
}

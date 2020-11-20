import 'package:json_annotation/json_annotation.dart';

// banner.g.dart 将在我们运行生成命令后自动生成
part 'banner.g.dart';

// 这个标注是告诉生成器，这个类是需要生成Model类的
// 自动生成指令 flutter packages pub run build_runner build
// 持续生成指令 flutter packages pub run build_runner watch
@JsonSerializable()
class Banner {
  Banner(this.picUrl, this.targetId, this.titleColor, this.url, this.typeTitle);

  @JsonKey(name: 'pic')
  String picUrl;

  int targetId;

  String titleColor = "white";

  String url;

  String typeTitle;

  factory Banner.fromJson(Map<String, dynamic> json) => _$BannerFromJson(json);

  Map<String, dynamic> toJson() => _$BannerToJson(this);
}

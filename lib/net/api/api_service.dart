import 'dart:io';

import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:light_music_flutter/models/banner_result.dart';

class ApiService {
  ApiService([this.context]) {
    _options = Options(extra: {"context": context});
  }
  BuildContext context;
  Options _options;

  static Dio dio = Dio(BaseOptions(baseUrl: 'http://118.31.65.24/', headers: {
    HttpHeaders.acceptHeader: "application/vnd.github.squirrel-girl-preview,"
        "application/vnd.github.symmetra-preview+json",
  }));

  static void init() {
    dio.options.headers[HttpHeaders.authorizationHeader] = "";
  }

  Future<BannerResult> getBanner({int type = 1}) async {
    var result = await dio.get("/banner?type=$type");
    return BannerResult.fromJson(result.data);
  }
}

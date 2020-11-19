import 'dart:convert';

import 'package:flutter/material.dart';


const _themes = <MaterialColor>[
  Colors.blue,
  Colors.cyan,
  Colors.teal,
  Colors.green,
  Colors.red,
];

class Global {
  // static SharedPreferences _prefrences;
  // static Profile profile = Profile();

  // static NetCache  netCache = NetCache();

  static List<MaterialColor> get themes => _themes;

  static bool get isRelease => bool.fromEnvironment("dart.vm.product");

  static Future<Null> init() async {
    // _prefrences = await SharedPreferences.getInstance();
    // var _profile = _prefrences.getString("profile");
    // if (_profile != null) {
    //   try{
    //     profile = Profile.fromJson(jsonDecode(_profile));
    //   } catch (e) {
    //     print(e);
    //   }
    // }

    // profile.cache = profile.cache ?? CacheConfig()
    // ..enable = true
    // ..maxAge = 3600
    // ..maxCount = 100;

    // Git.init();
  }

  //持久化Profile信息
  // static saveProfile() =>
  //   _prefrences.setString("profile", jsonEncode(profile.toJson()));
}

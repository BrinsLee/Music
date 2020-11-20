import 'package:flutter/material.dart';
import 'package:flutter_swiper/flutter_swiper.dart';
import 'package:light_music_flutter/models/banner_result.dart';
import 'package:light_music_flutter/net/api/api_service.dart';

class BannerWidget extends StatefulWidget {
  @override
  _BannerWidgetState createState() => _BannerWidgetState();
}

class _BannerWidgetState extends State<BannerWidget> {
  int _itemCount = 0;
  BannerResult _result;
  @override
  void initState() {
    super.initState();
    var data = ApiService(context).getBanner();

    data.then((value) {
      setState(() {
        _result = value;
        _itemCount = _result.banners.length;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      width: MediaQuery.of(context).size.width,
      margin: const EdgeInsets.all(5),
      height: 130,
      child: Swiper(
        itemCount: _itemCount,
        itemBuilder: _swiperBuilder,
        pagination: SwiperPagination(
          builder: DotSwiperPaginationBuilder(
              color: Colors.black38,
              activeColor: Colors.white,
              size: 5.0,
              activeSize: 5.0),
        ),
        control: null,
        duration: 300,
        scrollDirection: Axis.horizontal,
        autoplay: true,
        loop: true,
        itemWidth: MediaQuery.of(context).size.width - 10,
        // itemHeight: 150,
        // layout: SwiperLayout.TINDER,
        onTap: (index) {},
      ),
    );
  }

  Widget _swiperBuilder(BuildContext context, int index) {
    if (_result == null) {
      return Image.asset(
        "images/base_icon_default_cover.png",
        fit: BoxFit.cover,
      );
    } else {
      return (ClipRRect(
        borderRadius: BorderRadius.circular(16),
        child: FadeInImage.assetNetwork(
          placeholder: "images/base_icon_default_cover.png",
          image: _result.banners[index].picUrl,
          fit: BoxFit.cover,
        ),
      ));
    }
  }
}

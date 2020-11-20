import 'package:flutter/material.dart';

class DailyItem extends StatelessWidget {
  String imageUrl;
  String title;
  Function() onTap;
  DailyItem(this.imageUrl, this.title, this.onTap);
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Container(
            width: 50,
            height: 50,
            child: Image.asset(
              imageUrl,
              width: 50,
              height: 50,
            ),
          ),
          Text(
            title,
            style: TextStyle(color: Colors.black, fontSize: 12),
          )
        ],
      ),
      onTap: onTap,
    );
  }
}

import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';


class LoadingWidget extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Wrap(
        spacing: 10,
        direction: Axis.vertical,
        alignment: WrapAlignment.center,
        crossAxisAlignment: WrapCrossAlignment.center,
        children: [
          Container(
              width: 80,
              height: 80,
              child: SpinKitFadingCube(color: Colors.grey)),
          Text("loading ...",style: TextStyle(color: Colors.grey,fontSize: 13),)
        ],
      ),
    );
  }
}

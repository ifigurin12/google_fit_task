import 'package:flutter/material.dart';

class NotFoundPage extends StatelessWidget {
  static const String routeName = '/notFoundPage';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Ошибка 404'),
      ),
      body: Center(child: Text('Запрашиваемая страница - не найдена.'),),
    );
  }
}
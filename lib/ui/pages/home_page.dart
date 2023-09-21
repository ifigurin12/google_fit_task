import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:google_fit_test_task/bloc/bloc_auth/google_auth_bloc.dart';
import 'package:google_fit_test_task/ui/pages/auth_page.dart';

class HomePage extends StatefulWidget {
  static const routeName = '/homePage';

  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('HomePage'),
        actions: [
          IconButton(
            onPressed: () {},
            icon: Icon(Icons.exit_to_app),
          ),
        ],
      ),
      body: BlocListener<GoogleAuthBloc, GoogleAuthState>(
        listener: (context, state) {
          if (state is GoogleAuthInitial || state is GoogleAuthLoadingState) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                duration: Duration(seconds: 1),
                content: Text(
                  'Проверка входа в приложение',
                  style: TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.w600,
                    fontSize: 18.0,
                  ),
                ),
              ),
            );
          } else if (state is GoogleAuthFailureState) {
            Navigator.of(context)
                .pushNamedAndRemoveUntil(AuthPage.routeName, (route) => false);
          }
          else if (state is GoogleAuthSuccessState) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                duration: Duration(seconds: 1),
                backgroundColor: Colors.green,
                content: Text(
                  'Успешный вход в приложение',
                  style: TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.w600,
                    fontSize: 18.0,
                  ),
                ),
              ),
            );
          }
        },
        child: Center(child: Text('HomePage')),
      ),
    );
  }
}

import styled from "styled-components/native";
import {
  lightGray,
  primaryEnd,
  gray,
  lightGreen,
  danger,
  green
} from "../../utils/colors";

// === TEXT ===

export const TextWhite = styled.Text`
  font-weight: bold;
  font-size: 16px;
  text-align: center;
  color: #fefefe;
`;

export const TitlePage = styled.Text`
  text-align: center;
  font-size: 20px;
  color: palevioletred;
  font-weight: bold;
  margin-bottom: 15px;
  margin-top: 15px;
`;
export const Title = styled.Text`
  font-size: 17px;
  font-weight: bold;
  margin: 15px;
`;

// === WRAPPERS ===

export const Row = styled.View`
  padding: 10px;
  flex-direction: row;
  align-items: flex-end;
  /* background: papayawhip; */
`;

// Create a Wrapper component that'll render a <section> tag with some styles
export const Wrapper = styled.KeyboardAvoidingView`
  padding: 10px;
  flex: 1;
  /* background: papayawhip; */
`;

export const WrapperList = styled.View`
  padding: 10px;
  margin: 10px;
  border-bottom-width: 1px;
  border-top-width: 1px;
  border-radius: 10px;
  border-top-color: ${lightGray};
  border-bottom-color: ${lightGray};
  height: 65%;
  /* background: papayawhip; */
`;

export const WrapperForm = styled.View`
  padding: 10px;
  border-width: 1px;
  border-color: ${lightGray};
  border-radius: 5px;
`;

// === BUTTONS ===

export const AddButton = styled.TouchableOpacity`
  width: 60px;
  height: 60px;
  border-radius: 30px;
  background-color: ${primaryEnd};
  position: absolute;
  bottom: 15px;
  right: 15px;
  margin: 10px;
  padding: 10px;
  justify-content: center;
  align-items: center;
`;

export const AddNewBillButton = styled.TouchableOpacity`
  width: 60px;
  height: 60px;
  border-radius: 30px;
  background-color: ${green};
  align-self: flex-end;
  margin: 10px;
  padding: 10px;
  justify-content: center;
  align-items: center;
`;
export const CancelNewBillButton = styled.TouchableOpacity`
  width: 60px;
  height: 60px;
  border-radius: 30px;
  background-color: ${primaryEnd};
  align-self: flex-end;
  margin: 10px;
  padding: 10px;
  justify-content: center;
  align-items: center;
`;

export const EditMoneyButton = styled.TouchableOpacity`
  width: 60px;
  height: 30px;
  border-radius: 10px;
  background-color: ${gray};
  position: absolute;
  top: 15px;
  right: 15px;
  margin: 10px;
  padding: 15px;
  justify-content: center;
  align-items: center;
`;

export const TextAddButton = styled.Text`
  font-weight: bold;
  font-size: 25px;
  text-align: center;
  color: #fefefe;
`;

// === INPUTS ===

export const TextInputName = styled.TextInput`
  border-width: 1px;
  border-color: ${lightGray};
  font-size: 16px;
  margin-bottom: 15px;
  padding: 10px;
  border-radius: 10px;
`;
export const TextInputValue = styled.TextInput`
  border-width: 1px;
  border-color: ${lightGray};
  font-size: 16px;
  margin-bottom: 15px;
  padding: 10px;
  border-radius: 10px;
`;

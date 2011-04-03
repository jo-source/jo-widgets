/*
 * Copyright (c) 2011, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.common.types;

import java.util.HashMap;
import java.util.Map;

public enum VirtualKey {

	F1(null, "F1"),
	F2(null, "F2"),
	F3(null, "F3"),
	F4(null, "F4"),
	F5(null, "F5"),
	F6(null, "F6"),
	F7(null, "F7"),
	F8(null, "F8"),
	F9(null, "F9"),
	F10(null, "F10"),
	F11(null, "F11"),
	F12(null, "F12"),

	ENTER(null, "Enter"),
	BACK_SPACE(null, "Back Space"),
	TAB(null, "Tab"),
	SHIFT(null, "Shift"),
	CONTROL(null, "Ctrl"),
	ALT(null, "Alt"),
	PAUSE(null, "Pause"),
	CAPS_LOCK(null, "Caps Lock"),
	ESC(null, "Esc"),
	SPACE(null, "Space"),
	PAGE_UP(null, "Page up"),
	PAGE_DOWN(null, "Page down"),
	END(null, "End"),
	HOME(null, "Home"),

	ARROW_LEFT(null, "Arrow left"),
	ARROW_UP(null, "Arrow up"),
	ARROW_RIGHT(null, "Arrow right"),
	ARROW_DOWN(null, "Arrow down"),

	DIGIT_0(Character.valueOf('0'), "0"),
	DIGIT_1(Character.valueOf('1'), "1"),
	DIGIT_2(Character.valueOf('2'), "2"),
	DIGIT_3(Character.valueOf('3'), "3"),
	DIGIT_4(Character.valueOf('4'), "4"),
	DIGIT_5(Character.valueOf('5'), "5"),
	DIGIT_6(Character.valueOf('6'), "6"),
	DIGIT_7(Character.valueOf('7'), "7"),
	DIGIT_8(Character.valueOf('8'), "8"),
	DIGIT_9(Character.valueOf('9'), "9"),

	A(Character.valueOf('A'), "A"),
	B(Character.valueOf('B'), "B"),
	C(Character.valueOf('C'), "C"),
	D(Character.valueOf('D'), "D"),
	E(Character.valueOf('E'), "E"),
	F(Character.valueOf('F'), "F"),
	G(Character.valueOf('G'), "G"),
	H(Character.valueOf('H'), "H"),
	I(Character.valueOf('I'), "I"),
	J(Character.valueOf('J'), "J"),
	K(Character.valueOf('K'), "K"),
	L(Character.valueOf('L'), "L"),
	M(Character.valueOf('M'), "M"),
	N(Character.valueOf('N'), "N"),
	O(Character.valueOf('O'), "O"),
	P(Character.valueOf('P'), "P"),
	Q(Character.valueOf('Q'), "Q"),
	R(Character.valueOf('R'), "R"),
	S(Character.valueOf('S'), "S"),
	T(Character.valueOf('T'), "T"),
	U(Character.valueOf('U'), "U"),
	V(Character.valueOf('V'), "V"),
	W(Character.valueOf('W'), "W"),
	X(Character.valueOf('X'), "X"),
	Y(Character.valueOf('Y'), "Y"),
	Z(Character.valueOf('Z'), "Z"),

	/**
	 * The key pressed or released is not defined as a virtual key.
	 * This must not be used to define accelerators.
	 */
	UNDEFINED(null, "Undefined");

	private static Map<Character, VirtualKey> characterToVirtualKey = new HashMap<Character, VirtualKey>();

	private final Character character;
	private final String label;

	private VirtualKey(final Character character, final String label) {
		this.character = character;
		this.label = label;
	}

	/**
	 * //TODO I18N
	 * Gets the label of the key.
	 * 
	 * @return The label of the key
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Gets a character representation of the virtual key, or null, if the virtual key is not
	 * representable by a character.
	 * 
	 * @return The character representation or null
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * @return true, if the virtual key is representable by a character, false otherwise.
	 */
	public boolean isCharacter() {
		return character != null;
	}

	/**
	 * Gets the virtual key for a char.
	 * 
	 * @param character The char to get the virtual key for
	 * @return The virtual key for the char or null, if no virtual key exists for the char
	 */
	public static VirtualKey toVirtualKey(final char character) {
		return getCharacterToVirtualKey().get(Character.valueOf(character));
	}

	private static Map<Character, VirtualKey> getCharacterToVirtualKey() {
		if (characterToVirtualKey == null) {
			characterToVirtualKey.put(Character.valueOf('0'), VirtualKey.DIGIT_0);
			characterToVirtualKey.put(Character.valueOf('1'), VirtualKey.DIGIT_1);
			characterToVirtualKey.put(Character.valueOf('2'), VirtualKey.DIGIT_2);
			characterToVirtualKey.put(Character.valueOf('3'), VirtualKey.DIGIT_3);
			characterToVirtualKey.put(Character.valueOf('4'), VirtualKey.DIGIT_4);
			characterToVirtualKey.put(Character.valueOf('5'), VirtualKey.DIGIT_5);
			characterToVirtualKey.put(Character.valueOf('6'), VirtualKey.DIGIT_6);
			characterToVirtualKey.put(Character.valueOf('7'), VirtualKey.DIGIT_7);
			characterToVirtualKey.put(Character.valueOf('8'), VirtualKey.DIGIT_8);
			characterToVirtualKey.put(Character.valueOf('9'), VirtualKey.DIGIT_9);

			characterToVirtualKey.put(Character.valueOf('a'), VirtualKey.A);
			characterToVirtualKey.put(Character.valueOf('b'), VirtualKey.B);
			characterToVirtualKey.put(Character.valueOf('c'), VirtualKey.C);
			characterToVirtualKey.put(Character.valueOf('d'), VirtualKey.D);
			characterToVirtualKey.put(Character.valueOf('e'), VirtualKey.E);
			characterToVirtualKey.put(Character.valueOf('f'), VirtualKey.F);
			characterToVirtualKey.put(Character.valueOf('g'), VirtualKey.G);
			characterToVirtualKey.put(Character.valueOf('h'), VirtualKey.H);
			characterToVirtualKey.put(Character.valueOf('i'), VirtualKey.I);
			characterToVirtualKey.put(Character.valueOf('j'), VirtualKey.J);
			characterToVirtualKey.put(Character.valueOf('k'), VirtualKey.K);
			characterToVirtualKey.put(Character.valueOf('l'), VirtualKey.L);
			characterToVirtualKey.put(Character.valueOf('m'), VirtualKey.M);
			characterToVirtualKey.put(Character.valueOf('n'), VirtualKey.N);
			characterToVirtualKey.put(Character.valueOf('o'), VirtualKey.O);
			characterToVirtualKey.put(Character.valueOf('p'), VirtualKey.P);
			characterToVirtualKey.put(Character.valueOf('q'), VirtualKey.Q);
			characterToVirtualKey.put(Character.valueOf('r'), VirtualKey.R);
			characterToVirtualKey.put(Character.valueOf('s'), VirtualKey.S);
			characterToVirtualKey.put(Character.valueOf('t'), VirtualKey.T);
			characterToVirtualKey.put(Character.valueOf('u'), VirtualKey.U);
			characterToVirtualKey.put(Character.valueOf('v'), VirtualKey.V);
			characterToVirtualKey.put(Character.valueOf('w'), VirtualKey.W);
			characterToVirtualKey.put(Character.valueOf('x'), VirtualKey.X);
			characterToVirtualKey.put(Character.valueOf('y'), VirtualKey.Y);
			characterToVirtualKey.put(Character.valueOf('z'), VirtualKey.Z);

			characterToVirtualKey.put(Character.valueOf('A'), VirtualKey.A);
			characterToVirtualKey.put(Character.valueOf('B'), VirtualKey.B);
			characterToVirtualKey.put(Character.valueOf('C'), VirtualKey.C);
			characterToVirtualKey.put(Character.valueOf('D'), VirtualKey.D);
			characterToVirtualKey.put(Character.valueOf('E'), VirtualKey.E);
			characterToVirtualKey.put(Character.valueOf('F'), VirtualKey.F);
			characterToVirtualKey.put(Character.valueOf('G'), VirtualKey.G);
			characterToVirtualKey.put(Character.valueOf('G'), VirtualKey.H);
			characterToVirtualKey.put(Character.valueOf('I'), VirtualKey.I);
			characterToVirtualKey.put(Character.valueOf('J'), VirtualKey.J);
			characterToVirtualKey.put(Character.valueOf('K'), VirtualKey.K);
			characterToVirtualKey.put(Character.valueOf('L'), VirtualKey.L);
			characterToVirtualKey.put(Character.valueOf('M'), VirtualKey.M);
			characterToVirtualKey.put(Character.valueOf('N'), VirtualKey.N);
			characterToVirtualKey.put(Character.valueOf('O'), VirtualKey.O);
			characterToVirtualKey.put(Character.valueOf('P'), VirtualKey.P);
			characterToVirtualKey.put(Character.valueOf('Q'), VirtualKey.Q);
			characterToVirtualKey.put(Character.valueOf('R'), VirtualKey.R);
			characterToVirtualKey.put(Character.valueOf('S'), VirtualKey.S);
			characterToVirtualKey.put(Character.valueOf('T'), VirtualKey.T);
			characterToVirtualKey.put(Character.valueOf('U'), VirtualKey.U);
			characterToVirtualKey.put(Character.valueOf('V'), VirtualKey.V);
			characterToVirtualKey.put(Character.valueOf('W'), VirtualKey.W);
			characterToVirtualKey.put(Character.valueOf('X'), VirtualKey.X);
			characterToVirtualKey.put(Character.valueOf('Y'), VirtualKey.Y);
			characterToVirtualKey.put(Character.valueOf('Z'), VirtualKey.Z);
		}
		return characterToVirtualKey;
	}

}

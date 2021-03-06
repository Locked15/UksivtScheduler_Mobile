package com.authorization.uksivt_scheduler.custom_views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.authorization.uksivt_scheduler.R;
import com.authorization.uksivt_scheduler.interfaces.FavoriteGroupClickListener;
import com.authorization.uksivt_scheduler.user_data.Group;

import java.util.List;


/**
 * Класс-адаптер для списка избранных групп.
 */
public class FavoritesListAdapter extends BaseAdapter
{
	//region Область: Поля.

	/**
	 * Поле, содержащее любой класс, реализовавший интерфейс.
	 * <br/>
	 * Нужно для установки обработчиков событий на нажатия по элементам списка данного адаптера.
	 */
	public FavoriteGroupClickListener Listener;

	/**
	 * Поле, содержащее объект, нужный для заполнения элементов управления данными.
	 */
	private final LayoutInflater inflater;

	/**
	 * Поле, содержащее список данных о сохраненных группах.
	 */
	private final List<Group> groups;
	//endregion

	//region Область: Конструктор класса.

	/**
	 * Конструктор класса.
	 *
	 * @param context  Контекст приложения.
	 * @param groups   Список групп.
	 * @param listener Прослушиватель событий нажатий на элементы списка.
	 */
	public FavoritesListAdapter(Context context, List<Group> groups, FavoriteGroupClickListener listener)
	{
		this.groups = groups;
		inflater = LayoutInflater.from(context);
		Listener = listener;
	}
	//endregion

	//region Область: Реализация базового класса.

	/**
	 * Метод для получения элемента списка групп по его индексу.
	 *
	 * @param position Индекс нужного элемента.
	 * @return Элемент по этому индексу.
	 */
	@Override
	public Object getItem(int position)
	{
		return groups.get(position);
	}

	/**
	 * Метод для получения размера списка с группами.
	 *
	 * @return Размер списка.
	 */
	@Override
	public int getCount()
	{
		return groups.size();
	}

	/**
	 * Метод для возвращения связанного с типом "long" параметра-индекса.
	 *
	 * @param position Значение, которое нужно связать.
	 * @return Связанное с другим типом значение.
	 */
	@Override
	public long getItemId(int position)
	{
		return position;
	}

	/**
	 * Метод для установки внешнего вида элемента списка.
	 * <br>
	 * После установки вида, происходит наполнение данными.
	 *
	 * @param position    Индекс элемента.
	 * @param convertView Элемент, вызвавший событие.
	 * @param parent      Родительский элемент.
	 * @return Готовый и наполненный элемент.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Group data;
		ViewHolder holder;
		View toReturn = convertView;

		if (toReturn == null)
		{
			holder = new ViewHolder();
			toReturn = inflater.inflate(R.layout.favorite_element, parent, false);

			//region Подобласть: Инициализация значений "holder".
			holder.background = toReturn.findViewById(R.id.favorite_element_background);
			holder.favoriteName = toReturn.findViewById(R.id.favorite_element_header);
			holder.favoriteCourse = toReturn.findViewById(R.id.favorite_element_course);
			holder.favoriteBranch = toReturn.findViewById(R.id.favorite_element_branch);
			holder.favoriteSubGroup = toReturn.findViewById(R.id.favorite_element_sub_group);
			//endregion

			toReturn.setTag(holder);
		}

		holder = (ViewHolder)toReturn.getTag();
		data = groups.get(position);

		//region Подобласть: Установка значений полей.
		holder.favoriteCourse.setText(String.format(inflater.getContext().getString(R.string.course), data.calculateCourse()));
		holder.favoriteSubGroup.setText(String.format(inflater.getContext().getString(R.string.group_type), data.SubGroup));

		holder.favoriteName.setText(data.GroupName);
		holder.favoriteBranch.setText(data.getPrettyBranchName(toReturn.getContext()));

		if (Listener != null)
		{
			holder.background.setOnClickListener((Group) -> Listener.favoriteGroupClicked(data));
			holder.background.setOnLongClickListener((Group) ->
			{
				Listener.favoriteGroupLongClick(data);

				return false;
			});
		}
		//endregion

		return toReturn;
	}
	//endregion

	//region Область: "ViewHolder".

	/**
	 * Внутренний статический класс, нужный для хранения элементов управления.
	 */
	private static class ViewHolder
	{
		/**
		 * Задний фон элемента списка.
		 */
		private ConstraintLayout background;

		/**
		 * Название избранной группы.
		 */
		private TextView favoriteName;

		/**
		 * Курс избранной группы.
		 */
		private TextView favoriteCourse;

		/**
		 * Отделение избранной группы.
		 */
		private TextView favoriteBranch;

		/**
		 * Принадлежность избранной группы.
		 */
		private TextView favoriteSubGroup;
	}
	//endregion
}
